package model;

public enum UserType {
    ADMIN() {
        @Override
        public String getUserName() {
            return "admin";
        }
    },
    MANAGER() {
        @Override
        public String getUserName() {
            return "manager";
        }
    },
    WORKER() {
        @Override
        public String getUserName() {
            return "worker";
        }
    };
    abstract public String getUserName();

    public static String convert(UserType user) {
        return user.toString();
    }

    public static UserType convert(String user) {
        if (user.equalsIgnoreCase("admin")) {
            return UserType.ADMIN;
        }
        if (user.equalsIgnoreCase("manager")) {
            return UserType.MANAGER;
        }
        if (user.equalsIgnoreCase("worker")) {
            return UserType.WORKER;
        }
        return null;
    }

}
