import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Message {
    private final static Charset charset = StandardCharsets.UTF_8;
    private final static String msgSeparator = "#";
    private String msg;
    private String teamName;

    Message(String msg){
        this.msg = msg;
    }

    Message(String msg, String teamName){
        this.msg = msg;
        this.teamName = teamName;
    }

    Message(byte[] body) {
        msg = new String(body, charset);
    }

    public byte[] encode() {
        String result;
        if(teamName == null)
            result = msg;
        else
            result = teamName + msgSeparator + msg;
        return result.getBytes(charset);
    }

    public String decode(){
        if(teamName != null)
            return msg;

        String[] decoded = msg.split(msgSeparator, 2);
        msg = decoded[1];
        teamName = decoded[0];
        return msg;
    }

    public String getTeamName(){
        return teamName;
    }

}
