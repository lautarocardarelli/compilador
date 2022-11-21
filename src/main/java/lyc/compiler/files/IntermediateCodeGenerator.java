package lyc.compiler.files;


import lyc.compiler.model.TokenNotFoundException;
import lyc.compiler.model.VariableAlreadyDeclaredException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class IntermediateCodeGenerator implements FileGenerator {
    /* Singleton */
    private static IntermediateCodeGenerator icg;
    private IntermediateCodeGenerator() {
        this.stg = SymbolTableGenerator.getStgInstance();
    }
    public static IntermediateCodeGenerator getIcgInstance() {
        if (icg == null) {
            icg = new IntermediateCodeGenerator();
        }
        return icg;
    }
    /* Fin Singleton */

    /* DO Case*/

    public Stack<Integer> stackDoCase = new Stack<Integer>();
    public Stack<Integer> stackDoCaseFinal = new Stack<Integer>();
    /* All equal */
    boolean _firstExpList = true;
    Integer _i = 1;

    Stack<Integer> stackAllEqual = new Stack<Integer>();
    /* Fin All Equal */
    /* Para tipar Ids */
    private ArrayList<String> listIdsToAddType = new ArrayList<String>();

    public void setVariableType(String variableType) {
        this._variableType = variableType;
    }

    private String _variableType = "";

    /* Para condiciones y Saltos */

    /* rename */
    private Integer _saltoOr = null;

    public void setOpLogico(String opLogico) {
        this._opLogico = opLogico;
    }

    private String _opLogico = "";

    public void setComparador(String comparador) {
        this._comparador = comparador;
    }

    private String _comparador = "";

    private Stack<Object> stackWhile = new Stack<Object>();

    private Stack<Object> stackSaltoFuera = new Stack<Object>();
    private Stack<Object> stackSaltoDentro = new Stack<Object>();

    private Stack<Object> stackSaltoFinal = new Stack<Object>();

    public void invertCurrentComparador() {
        _comparador = getInvertedComparator(_comparador);
    }
    /* Fin condicones y saltos */

    private SymbolTableGenerator stg;
    private static Integer celdaActual = 0;
    private static Stack<Integer> cellStack = new Stack<>();

    private static Stack<Object> valueStack = new Stack<>();
    private List<String> polaca = new ArrayList<String>();

    public void insertarEnPolaca(Object o) {
        polaca.add(celdaActual, o.toString());
        celdaActual++;
    }

    public void updateCell(Integer cellNumber, String value) {
        polaca.set(cellNumber, value);
    }
    public void insertarEnPolaca(Object... objectArray) {
        for (Object o: objectArray) {
            polaca.add(celdaActual, o.toString());
            celdaActual++;
        }
    }

    /* Stacks */

    public void pushCurrentCell(Stack<Object> stack) {
        stack.push(polaca.size());
    }
    public void pushCurrentCellAndGo(Stack<Object> stack) {
        stack.push(celdaActual);
        insertarEnPolaca("NULL");
    }
    public Integer getCurrentCell() {
        return polaca.size();
    }

    public Object getTopStackAndPop(Stack<Object> stack){
        return stack.pop();
    }

    public void pushValue(Stack<Object> stack, Object value) {
        stack.push(value.toString());
    }
    public void pushValueAndGo(Stack<Object> stack, Object value){
        stack.push(value.toString());
        insertarEnPolaca("NULL");
    }

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        Integer index = 0;
        for (String p : polaca) {
            try {
                fileWriter.write(index.toString() + ": " + p + "\n");
                index++;
            } catch (Exception ex) {

            }
        }
    }

    public String getInvertedComparator(String comparator) {
        switch (comparator) {
            case ">":
                return "<=";
            case ">=":
                return "<";
            case "<":
                return ">=";
            case "<=":
                return ">";
            case "==":
                return "!=";
            case "!=":
                return "==";
            default:
                return comparator;

        }
    }
    public String getInvertedJump(String comparator) {
        switch (comparator) {
            case ">":
                return "BLE";
            case ">=":
                return "BLT";
            case "<":
                return "BGE";
            case "<=":
                return "BGT";
            case "==":
                return "BNE";
            case "!=":
                return "BE";
            default:
                return comparator;

        }
    }

    public String getJump(String comparator) {
        switch (comparator) {
            case ">":
                return "BGT";
            case ">=":
                return "BGE";
            case "<":
                return "BLT";
            case "<=":
                return "BLE";
            case "==":
                return "BE";
            case "!=":
                return "BNE";
            default:
                return comparator;

        }
    }

    public boolean idIsDeclaredSeveralTimes(String id) {
        boolean result = false;
        for (String savedId : listIdsToAddType) {
            if (savedId == id) {
                result = true;
            }
        }
        return result;
    }
    public void pushIdToType(String id) throws TokenNotFoundException, VariableAlreadyDeclaredException {
        if (!stg.isTokenSaved(id)) throw new TokenNotFoundException("Token " + id + "not found in TS");

        if (idIsDeclaredSeveralTimes(id)) throw new VariableAlreadyDeclaredException("Variable " + id + "already declared");

        listIdsToAddType.add(id);
    }

    public void addTypeToIds() {
        listIdsToAddType.forEach(id -> {
            stg.updateIdType(id, _variableType);
        });

        listIdsToAddType.clear();
        _variableType = "";
    }
    
    public void insertarComparadores() {
        insertarEnPolaca("CMP");
        if (_opLogico == "AND") {
            insertarEnPolaca(getInvertedJump(_comparador));
            /* Salto al false, bloque false */
            pushCurrentCellAndGo(stackSaltoFuera);
        }
        if (_opLogico == "OR") {
            insertarEnPolaca(getJump(_comparador));
            /* Salto al true */
            pushCurrentCellAndGo(stackSaltoDentro);
        }
    }

    /* IF */
    public void IfCondiciones() {
        if (_opLogico == "OR") {
            while (!stackSaltoDentro.empty()) {
                Integer celda = Integer.parseInt(stackSaltoDentro.pop().toString());
                Integer pointingCell = celdaActual + 2;
                updateCell(celda, pointingCell.toString());
            }
            insertarEnPolaca("BI");
            _saltoOr = celdaActual;
            insertarEnPolaca("NULL");
        }


        if (_opLogico == "") {
            insertarEnPolaca("CMP");
            insertarEnPolaca(getInvertedJump(_comparador));
            stackSaltoFuera.add(celdaActual);
        }
    }

    public void IfPostProgram() {
        insertarEnPolaca("BI");
        stackSaltoFinal.add(celdaActual);
        insertarEnPolaca("NULL");
    }

    public void IfSaltoElse () {
        if (_opLogico == "AND") {
            while (!stackSaltoFuera.empty()) {
                Integer celda = Integer.parseInt(stackSaltoFuera.pop().toString());
                updateCell(celda, celdaActual.toString());
            }
        }

        if (_opLogico == "OR") {
            updateCell(_saltoOr, celdaActual.toString());
        }

        if (_opLogico == "") {
            while (!stackSaltoFuera.empty()) {
                Integer celda = Integer.parseInt(stackSaltoFuera.pop().toString());
                updateCell(celda, celdaActual.toString());
            }
        }
    }

    public void IfSaltoAlFinal() {
        if (_opLogico == "") {
            while (!stackSaltoFuera.empty()) {
                Integer celda = Integer.parseInt(stackSaltoFuera.pop().toString());
                updateCell(celda, celdaActual.toString());
            }
        }

        while (!stackSaltoFinal.empty()) {
            Integer celda = Integer.parseInt(stackSaltoFinal.pop().toString());
            updateCell(celda, celdaActual.toString());
        }
    }

    /*While*/
    public void WhileInit() {
        stackWhile.add(celdaActual);
        insertarEnPolaca("WhileEtiq" + celdaActual.toString());
    }

    public void WhileCondBeforeProgram() {
        if (_opLogico == "OR") {
            while (!stackSaltoDentro.empty()) {
                Integer cellNumber = Integer.parseInt(stackSaltoDentro.pop().toString());
                Integer pointingCell = celdaActual + 2;
                updateCell(cellNumber, pointingCell.toString());
            }

            insertarEnPolaca("BI");
            pushCurrentCellAndGo(stackSaltoFinal);
        }

        if (_opLogico == "") {
            insertarEnPolaca("CMP");
            insertarEnPolaca(getInvertedJump(_comparador));
            /* Salto al false, bloque false */
            pushCurrentCellAndGo(stackSaltoFuera);
        }

    }

    public void WhileCondPostProgram() {
        if (_opLogico == "AND" || _opLogico == "") {
            while (!stackSaltoFuera.empty()) {
                Integer cellNumber = Integer.parseInt(stackSaltoFuera.pop().toString());
                Integer pointingCell = celdaActual;
                updateCell(cellNumber, pointingCell.toString());
            }
        }
    }
    public void WhileGoToEtiq() {
        insertarEnPolaca("BI");
        insertarEnPolaca(stackWhile.pop().toString());

        if (_opLogico == "OR") {
            while (!stackSaltoFinal.empty()) {
                Integer cellNumber = Integer.parseInt(stackSaltoFinal.pop().toString());
                updateCell(cellNumber, celdaActual.toString());
            }
        }
    }

    /* All Equal */

    public void AllEqualInit() {
        _firstExpList = true;
        _i = 1;
    }

    public void AllEqualEnd() {
        insertarEnPolaca("True", "@resul", "=");
        insertarEnPolaca("BI");
        Integer cell = celdaActual + 3;
        insertarEnPolaca(cell.toString());

        Integer falseIndex = celdaActual;
        insertarEnPolaca("False", "@result", "=");

        while (!stackAllEqual.empty()) {
            Integer cellFalse = stackAllEqual.pop();
            updateCell(cellFalse, falseIndex.toString());
        }
    }

    public void setAllEqualI(Integer i) {
        _i = i;
    }
    public void AllEqualFirstEQLista() {
        _firstExpList = false;
        _i = 1;
    }
    public void AllEqualCalcs(boolean increment) {
        if (increment) _i ++;
        if (_firstExpList) {
            insertarEnPolaca("@Aux" + _i, "=");
        } else {
            insertarEnPolaca("@Aux", "=");
            insertarEnPolaca("@Aux", "@Aux" + _i.toString(), "CMP", "BNE");
            stackAllEqual.add(celdaActual);
            insertarEnPolaca("NULL");
        }
    }

    /* Do Case */

    public void DoCaseConditions() {
        insertarEnPolaca("CMP", getInvertedJump(_comparador));
        stackDoCase.add(celdaActual);
        insertarEnPolaca("NULL");

    }

    public void DoCaseEndCase() {
        insertarEnPolaca("BI");
        stackDoCaseFinal.add(celdaActual);
        insertarEnPolaca("NULL");

        updateCell(stackDoCase.pop(), celdaActual.toString());
    }

    public void DoCaseUpdateSaltoFinal() {
        while (!stackDoCaseFinal.empty()) {
            updateCell(stackDoCaseFinal.pop(), celdaActual.toString());
        }
    }
}

