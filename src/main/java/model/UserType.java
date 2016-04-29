package model;

public enum UserType {
    ADMIN() {
        @Override
        public String getUserName() {
            return "admin";
        }

        @Override
        public String getPassword() {
            return "1230";
        }
    },
    MANAGER() {
        @Override
        public String getUserName() {
            return "manager";
        }

        @Override
        public String getPassword() {
            return "manager_password";
        }
    },
    WORKER() {
        @Override
        public String getUserName() {
            return "worker";
        }

        @Override
        public String getPassword() {
            return "worker_password";
        }
    };
    abstract public String getUserName();

    abstract public String getPassword();

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
