package com.functions;

/**
 * Represents a person with basic details.
 */
public abstract class Person {
    protected String name;
    protected String address;
    protected String phoneNumber;

    /**
     * Constructs a new Person with the specified details.
     *
     * @param name        the name of the person
     * @param address     the address of the person
     * @param phoneNumber the phone number of the person
     */
    public Person(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the name of the person.
     *
     * @return the name of the person
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the phone number of the person.
     *
     * @return the phone number of the person
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets the address of the person.
     *
     * @return the address of the person
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the name of the person.
     *
     * @param name the new name of the person
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the address of the person.
     *
     * @param address the new address of the person
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the phone number of the person.
     *
     * @param phoneNumber the new phone number of the person
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}