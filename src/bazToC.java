import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


public class bazToC {
    public static void main(String args[]) throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(chooser.FILES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter("(*.baz)", "baz"));
        chooser.showOpenDialog(null);
        String line = "";
        File bazFile = chooser.getSelectedFile();
        if (bazFile == null) {
            System.out.println("No file selected!!");
            return;
        }

        String filePath = chooser.getCurrentDirectory().getPath();
        String fileName = chooser.getSelectedFile().getName().substring(0, chooser.getSelectedFile().getName().indexOf('.')) +".c";  // .baz 제거
        File cFile = new File(Paths.get(filePath,fileName).toString()); // 파일 경로

        BufferedReader bufferedReader = new BufferedReader(new FileReader(bazFile));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(cFile));

        bufferedWriter.write("#include <stdio.h>\n");
        bufferedWriter.write("#include <stdlib.h>\n");
        bufferedWriter.write("#include <time.h>\n");
        bufferedWriter.write("#include <mem.h>\n");
        bufferedWriter.write("\n");
        bufferedWriter.write("#define ERROR 4\n");
        bufferedWriter.write("enum baz_val {TRUE, FALSE, BAZ};\n");
        bufferedWriter.write("void baz_print(enum baz_val v, int line) {\n");
        bufferedWriter.write("\tif (v == TRUE)\n");
        bufferedWriter.write("\t\tprintf(\"TRUE\\n\");\n");
        bufferedWriter.write("\telse if (v == FALSE)\n");
        bufferedWriter.write("\t\tprintf(\"FALSE\\n\");\n");
        bufferedWriter.write("\telse if (v == BAZ)\n");
        bufferedWriter.write("\t\tprintf(\"BAZ\\n\");\n");
        bufferedWriter.write("\telse\n");
        bufferedWriter.write("\t\tprintf(\"YOU ARE WRONG!@%d\\n\",line);\n");
        bufferedWriter.write("}\n");
        bufferedWriter.write("\n");

        bufferedWriter.write("void baz_get(enum baz_val *v, char * input, int line) {\n");
        bufferedWriter.write("\tif ( !strcmp( input,\"TRUE\" ) )\n");
        bufferedWriter.write("\t\t*v = TRUE;\n");
        bufferedWriter.write("\telse if ( !strcmp( input, \"FALSE\" ) )\n");
        bufferedWriter.write("\t\t*v = FALSE;\n");
        bufferedWriter.write("\telse if ( !strcmp( input, \"BAZ\" ) )\n");
        bufferedWriter.write("\t\t*v = BAZ;\n");
        bufferedWriter.write("\telse {\n");
        bufferedWriter.write("\t\tprintf(\"YOU ARE WRONG!@%d\\n\",line);\n");
        bufferedWriter.write("\t\t*v = ERROR;\n");
        bufferedWriter.write("\t}\n");
        bufferedWriter.write("}\n");
        bufferedWriter.write("\n");

        bufferedWriter.write("int baz_if(enum baz_val v, int line) {\n");
        bufferedWriter.write("\tif ( v == TRUE )\n");
        bufferedWriter.write("\t\treturn 1;\n");
        bufferedWriter.write("\telse if ( v == FALSE )\n");
        bufferedWriter.write("\t\treturn 0;\n");
        bufferedWriter.write("\telse if ( v == BAZ ) {\n");
        bufferedWriter.write("\t\tsrand( (unsigned) time(NULL) );\n");
        bufferedWriter.write("\t\treturn rand() % 2;\n");
        bufferedWriter.write("\t}\n");
        bufferedWriter.write("\telse {\n");
        bufferedWriter.write("\t\tprintf(\"YOU ARE WRONG!@%d\\n\",line);\n");
        bufferedWriter.write("\t\treturn 0;\n");
        bufferedWriter.write("\t}\n");
        bufferedWriter.write("}\n");
        bufferedWriter.write("\n");

        Map<Integer, ArrayList> layerMap = new HashMap<>();

        bufferedWriter.write("int main() {\n");
        bufferedWriter.write("\tchar tempBaz[30];\n");
        line = bufferedReader.readLine();
        int ifCount = 0;
        layerMap.put(ifCount, new ArrayList());
        boolean isContain;
        String temp, token, tap, tempLine;
        while (line != null) {
            System.out.println(line);
            StringTokenizer tokenizer = new StringTokenizer(line);
            tempLine = "";

            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
                switch (token) {
                    case "if":
                        temp = tokenizer.nextToken();
                        if (temp == null) {
                            tempLine += "\tif ( baz_if(ERROR, __LINE__) ) {";
                            break;
                        }
                        for (int i = 0; i < ifCount; ++i) {
                            tempLine += "\t";
                        }
                        isContain = false;  // 계층
                        for (int i = 0; i <= ifCount; ++i) {
                            isContain = layerMap.get(i).contains(temp);
                            if (isContain) {
                                break;
                            }
                        }
                        ++ifCount;
                        layerMap.put(ifCount, new ArrayList());

                        if(isContain) {
                            tempLine += "\tif ( baz_if("+temp+", __LINE__) ) {";
                        } else if (temp.equals("true") || temp.equals("false") || temp.equals("baz")){
                            tempLine += "\tif ( baz_if("+temp.toUpperCase()+", __LINE__) ) {";
                        }
                        else {
                            tempLine += "\tif ( baz_if(ERROR, __LINE__) ) {";
                        }

                        break;
                    case "if!":
                        temp = tokenizer.nextToken();
                        if (temp == null) {
                            tempLine += "\tif ( baz_if(ERROR, __LINE__) ) {";
                            break;
                        }
                        for (int i = 0; i < ifCount; ++i) {
                            tempLine += "\t";
                        }
                        isContain = false;  // 이전 계층을 포함해서 값이 있었는지
                        for (int i = 0; i <= ifCount; ++i) {
                            isContain = layerMap.get(i).contains(temp);
                            if (isContain) {
                                break;
                            }
                        }
                        ++ifCount;
                        layerMap.put(ifCount, new ArrayList());

                        if(isContain) { // 값이 이전 층들에서 쓰였는 지 확인함.
                            tempLine += "\tif ( !baz_if("+temp+", __LINE__) ) {";
                        } else if (temp.equals("true") || temp.equals("false") || temp.equals("baz")){
                            tempLine += "\tif ( !baz_if("+temp.toUpperCase()+", __LINE__) ) {";
                        }
                        else { // 값이 없었다면 4의 값인 ERROR를 줘서 c의 함수에서 잘못된 인자임을 알게 함.
                            tempLine += "\tif ( !baz_if(ERROR, __LINE__) ) {";
                        }
                        break;
                    case "endif" :
                        if (ifCount > 0) { // if가 있는지 확인
                            layerMap.remove(ifCount);
                            --ifCount;
                            for (int i = 0; i < ifCount; ++i) { // 들여쓰기
                                tempLine += "\t";
                            }
                            tempLine += "\t}";
                        }
                        else { // if에 대응되지 않는 endif가 나올 경우
                            tempLine += "\t{ printf(\"YOU ARE WRONG!@%d\\n\",__LINE__); }";
                        }
                        break;
                    case "end" :
                        for (int i = 0; i < ifCount; ++i) {
                            tempLine += "\t";
                        }
                        tempLine += "\treturn 0;";
                        break;
                    case "show":
                        for (int i = 0; i < ifCount; ++i) {
                            tempLine += "\t";
                        }
                        temp = tokenizer.nextToken();
                        isContain = false;
                        for (int i = 0; i <= ifCount; ++i) {
                            isContain = layerMap.get(i).contains(temp);
                            if (isContain) {
                                break;
                            }
                        }
                        if (temp.equals("true") || temp.equals("false") || temp.equals("baz")) {
                            tempLine += "\tbaz_print(" + temp.toUpperCase() + ", __LINE__);";
                        } else if(isContain) {
                            tempLine += "\tbaz_print(" + temp + ", __LINE__);";
                        } else {
                            tempLine += "\tbaz_print(ERROR, __LINE__);";
                        }

                        break;
                    case "get" : // get 으로 값을 입력받음.
                        temp = tokenizer.nextToken();

                        tap = "\t";
                        for (int i = 0; i < ifCount; ++i) {
                            tap += "\t";
                        }
                        isContain = false;
                        for (int i = 0; i <= ifCount; ++i) {
                            isContain = layerMap.get(i).contains(temp);
                            if (isContain) {
                                break;
                            }
                        }
                        if (!isContain) {
                            layerMap.get(ifCount).add(temp);
                            tempLine += tap + "enum baz_val " + temp +";\n";
                        }
                        tempLine += tap + "scanf(\"%s\", tempBaz);\n";
                        tempLine += tap + "baz_get(&"+temp+", " + "tempBaz, " + "__LINE__);";

                        break;
                    default: // 맨 처음에 변수가 올 경우!
                        tap = "\t";
                        for (int i = 0; i < ifCount; ++i) {
                            tap += "\t";
                        }
                        String temp1, temp2;
                        temp1 = tokenizer.nextToken();
                        temp2 = tokenizer.nextToken();
                        if (!temp1.equals("=")){
                            tempLine += tap + "printf(\"YOU ARE WRONG!@%d\\n\",__LINE__);";
                            break;
                        }
                        isContain = false;
                        for (int i = 0; i <= ifCount; ++i) {
                            isContain = layerMap.get(i).contains(token);
                            if (isContain) {
                                break;
                            }
                        }
                        boolean isContainRValue = false;
                        for (int i = 0; i <= ifCount; ++i) {
                            isContainRValue = layerMap.get(i).contains(temp2);
                            if (isContainRValue) {
                                break;
                            }
                        }

                        if (temp2.equals("true") || temp2.equals("false") || temp2.equals("baz")) {
                            if (!isContain) {
                                layerMap.get(ifCount).add(token);
                                tempLine += tap + "enum baz_val " + token + ";\n";
                            }
                            tempLine += tap + token + " ";
                            tempLine += "= " + temp2.toUpperCase()+";";
                        } else if (isContainRValue){
                            if (!isContain) {
                                layerMap.get(ifCount).add(token);
                                tempLine += tap + "enum baz_val " + token + ";\n";
                            }
                            tempLine += tap + token + " ";
                            tempLine += "= " + temp2 +";";
                        } else {
                            tempLine += tap + "printf(\"YOU ARE WRONG!@%d\\n\",__LINE__); //"+token+" " + temp1 +" " + temp2;
                        }
                }
            }
            tempLine += "\n";
            bufferedWriter.write(tempLine);
            line = bufferedReader.readLine();
        }


        bufferedWriter.write("}");
        bufferedReader.close();
        bufferedWriter.close();
    }
}
