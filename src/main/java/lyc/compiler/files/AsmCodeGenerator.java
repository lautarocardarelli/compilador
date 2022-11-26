package lyc.compiler.files;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.Queue;

public class AsmCodeGenerator implements FileGenerator {
    Queue<String> operandos = new LinkedList<>();
    IntermediateCodeGenerator icg = IntermediateCodeGenerator.getIcgInstance();
    SymbolTableGenerator stg = SymbolTableGenerator.getStgInstance();

    private String AsmAuxVariableName = "@AsmAux";
    private Integer AsmAuxIndex = 0;

    private ArrayList<Integer> jumpTo = new ArrayList<Integer>();
    public String getAsmAuxVariable() {
        String name =  AsmAuxVariableName + AsmAuxIndex;
        AsmAuxIndex ++;
        return name;
    }

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        fileWriter.write("include macros2.asm\n");
        fileWriter.write("include number.asm\n");
        fileWriter.write("\n");

        fileWriter.write(".MODEL \t LARGE\n");
        fileWriter.write(".386 \n");
        fileWriter.write(".STACK 200h \n");

        fileWriter.write("\n");
        fileWriter.write(".DATA\n");

        Formatter fmt = new Formatter();
        stg.simbolos.forEach(simbolo -> {
            String valor;
            if (simbolo.tipo == "ID") {
                valor = "?";
            } else {
                valor = simbolo.getValor();
            }
            fmt.format("%15s %15s %15s\n", simbolo.getNombre(), "dd", valor);
        });

        fileWriter.write(String.valueOf(fmt));
        fileWriter.write(".CODE\n");
        fmt.close();

        generateAssemblerCode(fileWriter);
    }

    public void generateAssemblerCode(FileWriter fileWriter) throws IOException {
        Formatter fmt = new Formatter();

        Integer i = 0;
        for (String p : icg.getPolaca()) {
            if (jumpTo.contains(i)) {
                jumpTo.remove(i);
                fmt.format("%15s\n", "Etiqueta" + i.toString() + ":");
            }
            String operando = stg.getToken(p);
            if (operando != null) {
                operandos.add(operando);
            } else {
                if (isBasicOperator(p)) {
                    writeAsm(fmt, "FLD", operandos.poll());
                    writeAsm(fmt, "FLD", operandos.poll());
                    writeAsmSingleCommand(fmt, getBasicOperatorCommand(p));
                    String auxVariable = getAsmAuxVariable();
                    writeAsm(fmt, "FSTP", auxVariable);
                    stg.save("ID", auxVariable);
                    operandos.add(auxVariable);
                }

                if (p == "=") {
                    writeAsm(fmt, "FLD", operandos.poll());
                    writeAsm(fmt, "FSTP", operandos.poll());
                }

                if (p == "CMP") {
                    writeAsm(fmt, "FLD", operandos.poll());
                    writeAsm(fmt, "FLD", operandos.poll());
                    writeAsmSingleCommand(fmt, "FXCH");
                    writeAsmSingleCommand(fmt, "FCOMP");
                    writeAsm(fmt, "FSTSW", "ax");
                    writeAsmSingleCommand(fmt, "SAHF");
                    writeAsmSingleCommand(fmt, "FFREE");
                }

                if (isJumpCommand(p)) {
                    writeAsm(fmt, getJumpCommand(p), "Etiqueta" + icg.getPolaca().get(i + 1));
                    jumpTo.add(Integer.parseInt(icg.getPolaca().get(i + 1)));
                }

                if (p == "WTR") {

                }

                if (p == "READ") {

                }
            }


            i ++;
        }

        fileWriter.write(String.valueOf(fmt));
    }

    public String getBasicOperatorCommand(String operador) {
        switch (operador) {
            case "+":
                return "FADD";
            case "-":
                return "FSUB";
            case "/":
                return "FDIV";
            case "*":
                return "FMUL";
            default:
                return "";
        }
    }

    public boolean isBasicOperator(String operador) {
        if (operador == "+" || operador == "-" || operador == "/" || operador == "*") {
            return true;
        } else {
            return false;
        }
    }

    public void writeAsm(Formatter fmt, String command, String inp) {
        fmt.format("\t%15s %15s\n", command,inp);
    }

    public void writeAsmSingleCommand(Formatter fmt, String command) {
        fmt.format("\t%15s\n", command);
    }

    public boolean isJumpCommand(String icgCommand) {
        if (icgCommand == "BLE" || icgCommand == "BLT" || icgCommand == "BGE" || icgCommand == "BGT" || icgCommand == "BNE" || icgCommand == "BE" || icgCommand == "BI") {
            return true;
        }
        return false;
    }
    public String getJumpCommand(String icgCommand) {
        switch (icgCommand) {
            case "BLE":
                return "JBE";
            case "BLT":
                return "JNAE";
            case "BGE":
                return "JNB";
            case "BGT":
                return "JNBE";
            case "BNE":
                return "JNE";
            case "BE":
                return "JE";
            case "BI":
                return "JMP";
            default:
                return "";
        }
    }
}
