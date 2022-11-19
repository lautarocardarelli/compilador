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

    private ArrayList<String> listIdsToAddType = new ArrayList<String>();
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

    public void addTypeToIds(String type) {
        listIdsToAddType.forEach(id -> {
            stg.updateIdType(id, type);
        });

        listIdsToAddType.clear();
    }
}

