import java.util.Scanner;

public class Test1 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.print("请输入：");
        String s=scanner.nextLine();
        while (s.length()>0){
            System.out.print("/r"+s+"\r请输入\r：");
            s=scanner.nextLine();
        }
    }
}
