package backend.utils.enums;

public enum Action {
    LOGIN,
    SIGNUP,
    UPLOAD_POST,
    GET_POST,
    EDIT_POST,
    DELETE_POST,
    LIKE_POST,
    UNLIKE_POST,
    REPOST,
    COMMENT,
    UNCOMMENT,
    REPLY,
    FOLLOW,
    UNFOLLOW,
    GET_FOLLOWERS,
    GET_FOLLOWING,
    GET_PROFILE,
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


//  11/17