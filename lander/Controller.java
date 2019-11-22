package lander;
import java.util.Arrays;
public class Controller {
    Model lander;
    public Controller(Model model) {
        lander = model;
    }

    public String handle(String[] message) {
        String reply = "";
        /* first line of message */
        String[] type = message[0].split(":");
        int lines = message.length; /*count lines after first*/
        String[] payload = Arrays.copyOfRange(message,1,lines);
        switch( type[0] ) {
        case "state":
            reply = doState(payload);
            break;
        case "condition":
            reply = doCondition(payload);
            break;
        case "command":
            reply = doCommand(payload);
            break;
		case "terrain":
			reply = doTerrain(payload);
			break;
        }
        return reply;
    }

    String doState(String[] content) {
        StringBuffer response = new StringBuffer("state:=\n");
        response = response.append("x:"+lander.x+"\n");
        response = response.append("y:"+lander.y+"\n");
        response = response.append("O:"+lander.O+"\n");
        response = response.append("x':"+lander.xdot+"\n");
        response = response.append("y':"+lander.ydot+"\n");
        response = response.append("O':"+lander.Odot+"\n");

        return response.toString();
    }

    String doCondition(String[] content) {
        StringBuffer response = new StringBuffer("condition:=\n");
        response = response.append("fuel:"+lander.fuel+"%\n");
        response = response.append("altitude:"+lander.altitude+"\n");
        response = response.append("contact:");
        if( lander.isflying ) response = response.append("flying");
        else {
            if( lander.iscrashed ) response.append("crashed");
            else                   response.append("down");
        }
        response.append("\n");

        return response.toString();
    }

    String doCommand(String[] content) {
        StringBuffer response = new StringBuffer("command:=\n");
        for(String line : content) {
            String[] pair = line.split(":");
            switch( pair[0] ) {
            case "main-engine":
                lander.throttle = Double.parseDouble( pair[1] );
                break;
            case "rcs-roll":
                lander.roll = Double.parseDouble( pair[1] );
                break;
            }
        }

        return response.toString();
    }

	String doTerrain(String[] content) {
		int[] offset = {-25,-20,-15,-10,-5,0,5,10,15,20,15};
		int[] xdata = new int[11];
		int[] ydata = new int[11];
		StringBuffer response = new StringBuffer("terrain:=\n");

		for(int n=0; n<11; n++){
			xdata[n] = (int)(lander.x+offset[n]);
			ydata[n] = lander.ground[xdata[n]];
		}
		response.append("points: 11\n");

		String xs = Arrays.toString(xdata);
		response.append("data-x: "+ xs.substring(1, xs.lastIndexOf(']')) + "\n");

		String ys = Arrays.toString(ydata);
		response.append("data-y: "+ ys.substring(1, ys.lastIndexOf(']')) + "\n");

		return response.toString();
	}

}
