package y.userservice.exception;

public class FollowerAlreadyFollowingException extends RuntimeException{

        public FollowerAlreadyFollowingException(String message) {
            super(message);
        }
}
