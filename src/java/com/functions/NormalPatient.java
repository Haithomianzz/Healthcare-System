package com.functions;

/**
 * Represents a normal patient.
 */
public class NormalPatient extends Patient {

    /**
     * Constructs a new NormalPatient with the specified details.
     *
     * @param name          the name of the patient
     * @param address       the address of the patient
     * @param phoneNumber   the phone number of the patient
     * @param gender        the gender of the patient
     * @param symptoms      the symptoms of the patient
     * @param paymentMethod the payment method of the patient
     * @param emergency     whether the patient is an emergency case
     * @param doctor        the ID of the doctor in charge
     */
    public NormalPatient(String name, String address, String phoneNumber, String gender,
                         String symptoms, String paymentMethod, boolean emergency, int doctor) {
        super(name, address, phoneNumber, gender, symptoms, paymentMethod, emergency, doctor);
    }

    /**
     * Constructs a new NormalPatient with the specified details.
     *
     * @param ID            the ID of the patient
     * @param name          the name of the patient
     * @param address       the address of the patient
     * @param phoneNumber   the phone number of the patient
     * @param gender        the gender of the patient
     * @param symptoms      the symptoms of the patient
     * @param paymentMethod the payment method of the patient
     * @param emergency     whether the patient is an emergency case
     * @param doctor        the ID of the doctor in charge
     */
    public NormalPatient(int ID, String name, String address, String phoneNumber, String gender,
                         String symptoms, String paymentMethod, boolean emergency, int doctor) {
        super(ID, name, address, phoneNumber, gender, symptoms, paymentMethod, emergency, doctor);
    }
}
