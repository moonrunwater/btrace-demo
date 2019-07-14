import java.util.Random;

public class App {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws Exception {
        while (true) {
            print("name_" + RANDOM.nextInt(10), RANDOM.nextInt(100));
            Thread.sleep(RANDOM.nextInt(5) * 1000);
        }
    }

    private static Person print(String name, int age) {
        System.out.printf("name=%s, age=%d\n", name, age);
        return new Person(name, age);
    }

    static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public Person() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

}
