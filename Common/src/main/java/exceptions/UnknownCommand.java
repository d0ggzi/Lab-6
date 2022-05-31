package exceptions;

public class UnknownCommand extends Exception{
    String cmnd = "";
    public UnknownCommand(String str){
        cmnd = str;
    }

    @Override
    public String toString(){
        return "Неопознанная команда - " + cmnd + ". Наберите 'help' для справки";
    }
}
