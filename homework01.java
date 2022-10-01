

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class homework01 {
    public static void main(String[] args) throws IOException {
        int judge;
        System.out.println("请输入所输出式子的个数");
        Scanner in =new Scanner(System.in);
        judge= in.nextInt();
        int[]nums=new int[3];
        int[]symbols=new int[2];
        //使用Set保证不会存储相同式子
        HashSet<culNum> culNums = new HashSet<>();
        while(culNums.size()!=judge){
            for (int i = 0; i < 3; i++) {
                nums[i]=(int)(Math.random()*100);
            }
            for (int i = 0; i < 2; i++) {
                symbols[i]=(int)(Math.random()*3);
            }

            //symbols[0]等于0，代表第一个运算符为空，此时将第一个数置为0
            if(symbols[0]==0)
                nums[0]=0;
            //symbols[1]等于0，代表第二个运算符为空，此时将第三个数置为0
            if(symbols[1]==0)
                nums[2]=0;

            //当三个数字都不为0，同时两个运算符都不为0使，存入culNums中
            if(nums[0]+nums[1]+nums[2]!=0&&symbols[0]+symbols[1]!=0){
                culNums.add(new culNum(nums[0],nums[1],nums[2],symbols[0],symbols[1]));
            }
        }
        //integers用以存储每个式子对应的答案
        ArrayList<Integer> integers = new ArrayList<>();

        for (culNum e:culNums
             ) {
            integers.add(culFunction(e.toString()));
        }

        //将算式和答案分别存储到对应文件
        File file=new File("src\\Exercises.txt");//算式
        File file1=new File("src\\Answers.txt");//答案
        BufferedWriter bufferedWriter;
        file.createNewFile();
        file1.createNewFile();
        bufferedWriter=new BufferedWriter(new FileWriter(file));
        for (culNum e:culNums
        ) {
            bufferedWriter.write(e.toString());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
        bufferedWriter=new BufferedWriter(new FileWriter(file1));
        for (int e:integers
        ) {
            System.out.println(e);
            bufferedWriter.write(e+"");
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
        System.out.println("已完成");


    }

    //利用栈实现计算功能
    public static int culFunction(String str){
        //数字栈
        Stack<Integer> integers = new Stack<>();
        //字符栈
        Stack<Character> characters = new Stack<>();
        String temp=str;
        String numsCul="";
        char[]proceed=temp.toCharArray();
        for (int i = 0; i < proceed.length; i++) {
            if(Character.isDigit(proceed[i])){
                while(Character.isDigit(proceed[i])){
                    numsCul+=proceed[i];
                    if(i== proceed.length-1){
                        break;
                    }
                    i++;
                }
                integers.push(Integer.parseInt(numsCul));
                numsCul="";
                if(i!=proceed.length-1)
                    --i;

            }
            else {
                //若字符栈为空则直接进栈
                if(characters.empty()){
                    characters.push(proceed[i]);
                }
                //若不为空，当要入栈的操作字符优先级小于等于字符栈头部字符时，
                //从数字栈中取出两个数值，在字符栈中取出一个操作符，进行运算并将结果放入数字栈中，
                //若要入栈的操作字符优先级大于字符栈头部字符时，直接入栈
                else {
                    while(Symbol(proceed[i])<=Symbol(characters.peek())){
                        integers.push(cul(integers.pop(),integers.pop(),characters.pop()));
                        if(characters.empty())
                            break;
                    }
                    characters.push(proceed[i]);
                }

            }
        }
        //若字符栈中仍有操作符，则循环运算直至算出结果
        while(characters.size()!=0){
            integers.push(cul(integers.pop(),integers.pop(),characters.pop()));
        }
        return integers.peek();
    }

    //判断运算符优先级,* /为 2,其他为1
    public static int Symbol(char a){
        if(a=='*'||a=='/')
            return 2;
        else return 1;
    }

    public static int cul(int num2,int num1,char operate){
        switch (operate){
            case '+':return num1+num2;
            case '-':return num1-num2;
            case '*':return num1*num2;
            default:return num1/num2;
        }
    }
}


/**
 * 该类用以表示计算式
 */
class culNum{
    // num代表所计算的数字
    int num1,num2,num3;
    //culSymbol代表计算符号，1代表+，2代表-，0代表该符号为空
    int culSymbol1,culSymbol2;

    public culNum(int num1, int num2, int num3, int culSymbol1, int culSymbol2) {
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.culSymbol1 = culSymbol1;
        this.culSymbol2 = culSymbol2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        culNum culNum = (culNum) o;
        return num1 == culNum.num1 && num2 == culNum.num2 && num3 == culNum.num3 && culSymbol1 == culNum.culSymbol1 && culSymbol2 == culNum.culSymbol2;
    }


    @Override
    public String toString() {
        //下列三个字符串用以输出三个数字，便于将数字置为空
        String num1String=num1+"",num2String=num2+"",num3String=num3+"";
        //当第一个运算符为空时，防止出现第一个数字为0，第二个数字不为0，显示时出现0XX的情况
        if(culSymbol1==0){
            //当第一个数字和第二个数字都为0的时候，保留第二个0，第一个数字置为空
            if(num1+num2==0){
                num1String="";
            }
            //第一个数字为0，第二个数字不为0时，也将第一个数字置为空
            else if (num1==0&&num2!=0){
                num1String="";
            }
            //第一个数字不为0，第二个数字为0时，也将第二个数字置为空
            else if(num1!=0&&num2==0){
                num2String="";
            }
        }
        //下列处理第二个运算符为空的步骤与上面相同
        if(culSymbol2==0){
            if(num2+num3==0){
                num3String="";
            }else if (num2==0&&num3!=0){
                num2String="";
            }else if(num2!=0&&num3==0){
                num3String="";
            }
        }
        return num1String+symbolToString(culSymbol1)+num2String+symbolToString(culSymbol2)+num3String;
    }

    private String symbolToString(int symbol){
        if(symbol==1)
            return "+";
        else if(symbol==0)
            return "";
        else return "-";
    }

    @Override
    public int hashCode() {
        return Objects.hash(num1, num2, num3, culSymbol1, culSymbol2);
    }
}
