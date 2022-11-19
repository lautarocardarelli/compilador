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
        polaca.add(o.toString());
        celdaActual++;
    }

    public void insertarEnPolaca(Object... objectArray) {
        for (Object o: objectArray) {
            polaca.add(o.toString());
            celdaActual++;
        }
    }

    /* Stacks */

    public void pushCurrentCell(Stack<Object> stack) {
        stack.push(polaca.size());
    }
    public void pushCurrentCellAndGo(Stack<Object> stack) {
        stack.push(polaca.size());
        celdaActual++;
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
        celdaActual++;
    }

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        polaca.forEach(p -> {
            try {
                fileWriter.write(p + "\t");
            } catch (Exception ex) {

            }
        });
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
            insertarEnPolaca(getInvertedComparator(_comparador));
            /* Salto al false, bloque false */
            pushCurrentCellAndGo(stackSaltoFuera);
        } else {
            insertarEnPolaca(_comparador);
            pushCurrentCellAndGo(stackSaltoDentro);
        }
    }


    /* IF SALTOS*/

    public void IFSalto() {
        if (_opLogico == "OR") {
            while (!stackSaltoDentro.empty()) {
                Integer celda = Integer.parseInt(stackSaltoDentro.pop().toString());

                /* Extraer a un metodo */
                polaca.add(celda, Integer.toString(polaca.size() + 2));

                insertarEnPolaca("BI");
                _saltoOr = celdaActual;
                celdaActual ++;

            }
        }

        if (_opLogico == "") {
            insertarEnPolaca("CMP");
            insertarEnPolaca(getInvertedComparator(_comparador));
            stackSaltoFuera.add(celdaActual);
            celdaActual ++;
        }
    }

    public void IFProgram() {
        insertarEnPolaca("BI");
        stackSaltoFinal.add(celdaActual);
        celdaActual ++;
    }

    public void IFSaltoElse () {
        if (_opLogico == "AND") {
            while (!stackSaltoFuera.empty()) {
                Integer celda = Integer.parseInt(stackSaltoFuera.pop().toString());
                polaca.add(celda, celdaActual.toString());
            }
        }

        if (_opLogico == "OR") {
            polaca.add(_saltoOr, celdaActual.toString());

        }

        if (_opLogico == "") {
            while (!stackSaltoFuera.empty()) {
                Integer celda = Integer.parseInt(stackSaltoFuera.pop().toString());
                polaca.add(celda, celdaActual.toString());
            }
        }
    }

    public void IFSaltoFinal() {
        while (!stackSaltoFinal.empty()) {
            Integer celda = Integer.parseInt(stackSaltoFinal.pop().toString());
            polaca.add(celda, celdaActual.toString());
        }
    }
}

