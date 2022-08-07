import java.util.Scanner;

public class Test1 {
    public static void main(String[] args) {
        for(int i=0;i<10;i++){
            System.out.println("##############################");
        }
        Scanner scanner=new Scanner(System.in);
        String s;
        while(true){
            s=scanner.nextLine();
            if(s.equals("w")){
                System.out.print("\u001b[{n}A");
            } else if (s.equals("s")) {
                System.out.print("\u001b[{n}B");
            } else if (s.equals("a")) {
                System.out.print("\u001b[{n}C");
            }else {
                System.out.print("\u001b[{n}D");
            }
        }
    }
}
