public class InviteLinkCommand implements Command {
    private final String inviteLink;
    private final long chat_id;

    InviteLinkCommand(String inviteLink, long chat_id) {
        this.inviteLink = inviteLink;
        this.chat_id = chat_id;
    }

    @Override
    public String execute() {
        if (inviteLink == null) {
            return "You did not specify the invite link. Please type it in the format /invitelink [invite link]!";
        }

        if (MixnMatchBot.map.get(chat_id).getFinalizeStatus()) {
            return "You have finalize your order! If you want to reset, please make another group!";
        }

        if (MixnMatchBot.map.containsKey(chat_id)) {
            ClientOrder co = MixnMatchBot.map.get(chat_id);
            if (co.getFinalizeStatus()) {
                return "You have finalize your order! If you want to reset, please make another group!";
            }
            co.setInviteLink(inviteLink);
        } else {
            ClientOrder co = new ClientOrder();
            co.setInviteLink(inviteLink);
            MixnMatchBot.map.put(chat_id, co);
        }

        return "Successfully adding the invite link!";
    }
}
