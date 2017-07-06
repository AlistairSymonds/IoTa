public class TestMain{
    public static void main(String args[]){
        if(args.length > 0){
            System.out.println("Opening test app" + args[0]);
             if(args[0].equalsIgnoreCase("blaster")){
                    Blaster.testSpam();
            }
        } else {
            System.out.println("No args specified :(");
            System.out.println("Maybe try \"help\"?");
        }

    }
}