package bot.command;

import bot.utility.ClientOrder;

import java.util.Map;

public class InviteLinkCommand implements Command {
    private final Map<Long, ClientOrder> map;
    private final String inviteLink;
    private final long chat_id;

    public InviteLinkCommand(Map<Long, ClientOrder> map, String inviteLink, long chat_id) {
        this.map = map;
        this.inviteLink = inviteLink;
        this.chat_id = chat_id;
    }

    @Override
    public String execute() {
        if (inviteLink == null) {
            return "You did not specify the invite link. Please type it in the format /invitelink [invite link]!";
        }


        if (inviteLink.startsWith("https://t.me/joinchat/")) {
            if (this.map.containsKey(chat_id)) {
                ClientOrder co = this.map.get(chat_id);
                if (co.getFinalizeStatus()) {
                    return "You have finalize your order! If you want to reset, please make another group!";
                }
                co.setInviteLink(inviteLink);
            } else {
                    ClientOrder co = new ClientOrder();
                    co.setInviteLink(inviteLink);
                    this.map.put(chat_id, co);
            }

            return "Successfully adding the invite link!";

        } else {
            return "Invalid telegram group link!";
        }
    }
}
