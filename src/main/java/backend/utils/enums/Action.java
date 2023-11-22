package backend.utils.enums;

public enum Action {
    LOGIN,
    SIGNUP,
    UPLOAD_POST,
    EDIT_POST,
    DELETE_POST,
    LIKE_POST,
    COMMENT,
    REPLY,
    REPOST,
    FOLLOW,
    ACCEPT_FOLLOW,
    UNFOLLOW,
    EDIT_PROFILE;
    public boolean requiresToken() {
        switch (this) {
            case LOGIN:
            case SIGNUP:
                return false;
            default:
                return true;
        }
    }
}
