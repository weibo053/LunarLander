package me.themgrf.lunarlander.handlers;

import java.util.Arrays;

/**
 * Controller for handling different lander conditions
 */
public class Controller {

    private final Model lander;

    /**
     * Constructor for creating a controller and assigning a model
     * @param model The lander model
     */
    public Controller(Model model) {
        lander = model;
    }

    /**
     * Set the handle reply type
     * @param message The message to get the reply from
     * @return The reply handle type
     */
    public String handle(String[] message) {
        String[] type = message[0].split(":"); // First line of message
        int lines = message.length; // Count lines after first
        String[] payload = Arrays.copyOfRange(message, 1, lines);

        String reply;
        switch (type[0]) {
            case "state":
                reply = setState();
                break;
            case "condition":
                reply = setCondition();
                break;
            case "command":
                reply = setCommand(payload);
                break;
            case "terrain":
                reply = setTerrain();
                break;
            default:
                reply = "";
                break;
        }
        return reply;
    }

    /**
     * Set the state of the lander
     * @return The formatted state of the lander
     */
    private String setState() {
        return "state:=\n" + "x:" + lander.x + "\n" +
                "y:" + lander.y + "\n" +
                "O:" + lander.O + "\n" +
                "x':" + lander.xdot + "\n" +
                "y':" + lander.ydot + "\n" +
                "O':" + lander.Odot + "\n";
    }

    /**
     * Set the condition of the lander
     * @return The formatted condition of the lander
     */
    private String setCondition() {
        StringBuilder response = new StringBuilder("condition:=\n");
        response.append("fuel:").append(lander.fuel).append("%\n");
        response.append("altitude:").append(lander.altitude).append("\n");
        response.append("contact:");

        if (lander.isFlying) {
            response.append("flying");
        } else {
            response.append(lander.isCrashed ? "crashed" : "down");
        }
        response.append("\n");

        return response.toString();
    }

    /**
     * Set the command for the lander
     * @param content The argument for the lander command
     * @return The command for the lander
     */
    private String setCommand(String[] content) {
        for (String line : content) {
            String[] pair = line.split(":");
            switch (pair[0]) {
                case "main-engine":
                    lander.throttle = Double.parseDouble(pair[1]);
                    break;
                case "rcs-roll":
                    lander.roll = Double.parseDouble(pair[1]);
                    break;
                default:
                    break;
            }
        }

        return "command:=\n";
    }

    /**
     * Set the terrain for the lander
     * @return The terrain for the lander
     */
    private String setTerrain() {
        int[] offset = {-25, -20, -15, -10, -5, 0, 5, 10, 15, 20, 15};
        int[] xdata = new int[11];
        int[] ydata = new int[11];
        StringBuilder response = new StringBuilder("terrain:=\n");

        for (int n = 0; n < 11; n++) {
            xdata[n] = (int) (lander.x + offset[n]);
            ydata[n] = lander.ground[xdata[n]];
        }
        response.append("points: 11\n");

        String xs = Arrays.toString(xdata);
        response.append("data-x: ").append(xs.substring(1, xs.lastIndexOf(']'))).append("\n");

        String ys = Arrays.toString(ydata);
        response.append("data-y: ").append(ys.substring(1, ys.lastIndexOf(']'))).append("\n");

        return response.toString();
    }

}
