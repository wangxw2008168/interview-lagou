package com.lagou.interview;

import java.util.Arrays;

/**
 * 克隆相关示例
 */
public class CloneExample {
    public static void main(String[] args) throws CloneNotSupportedException {
        ArraysCopy(); // Arrays.copyOf 浅克隆
//        cloneTest(); // 克隆
    }

    /**
     * Arrays.copyOf 浅克隆
     */
    private static void ArraysCopy() {
        People[] o1 = {new People(1, "Java",
                new Address(110, "北京"))};
        People[] o2 = Arrays.copyOf(o1, o1.length);
        // 修改原型对象的第一个元素的值
        o1[0].setName("Jdk");
        System.out.println("o1:" + o1[0].getName());
        System.out.println("o2:" + o2[0].getName());
    }

    /**
     * 克隆
     */
    private static void cloneTest() throws CloneNotSupportedException {
        // 创建被赋值对象
        Address address = new Address(110, "北京");
        People p1 = new People(1, "Java", address);
        // 克隆 p1 对象
        People p2 = p1.clone();
        // 修改原型对象
        p1.getAddress().setCity("西安");
        // 输出 p1 和 p2 地址信息
        System.out.println("p1:" + p1.getAddress().getCity() +
                " p2:" + p2.getAddress().getCity());
    }

    /**
     * 用户类
     */
    static class People implements Cloneable {
        private Integer id;
        private String name;
        private Address address;

        /**
         * 重写 clone 方法
         * @throws CloneNotSupportedException
         */
        @Override
        protected People clone() throws CloneNotSupportedException {
            People people = (People) super.clone();
            people.setAddress(this.address.clone());
            return people;
        }

        public People() {
        }

        public People(Integer id, String name, Address address) {
            this.id = id;
            this.name = name;
            this.address = address;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }
    }

    /**
     * 地址类
     */
    static class Address implements Cloneable {
        private Integer id;
        private String city;

        /**
         * 重写 clone 方法
         * @throws CloneNotSupportedException
         */
        @Override
        protected Address clone() throws CloneNotSupportedException {
            return (Address) super.clone();
        }

        public Address() {
        }

        public Address(Integer id, String city) {
            this.id = id;
            this.city = city;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
