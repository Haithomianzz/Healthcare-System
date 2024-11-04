package com.functions;

/**
 * Represents an emergency patient.
 */
public class EmergencyPatient extends Patient {

    /**
     * Constructs a new EmergencyPatient with the specified details.
     *
     * @param name          the name of the patient
     * @param address       the address of the patient
     * @param phoneNumber   the phone number of the patient
     * @param gender        the gender of the patient
     * @param symptoms      the symptoms of the patient
     * @param paymentMethod the payment method of the patient
     * @param roomNumber    the room number assigned to the patient
     * @param emergency     whether the patient is an emergency case
     * @param doctor        the ID of the doctor in charge
     */
    public EmergencyPatient(String name, String address, String phoneNumber, String gender,
                            String symptoms, String paymentMethod, int roomNumber, boolean emergency, int doctor) {
        super(name, address, phoneNumber, gender, symptoms, paymentMethod, emergency, doctor);
        setRoomNumber(roomNumber);
    }

    /**
     * Constructs a new EmergencyPatient with the specified details.
     *
     * @param ID            the ID of the patient
     * @param name          the name of the patient
     * @param address       the address of the patient
     * @param phoneNumber   the phone number of the patient
     * @param gender        the gender of the patient
     * @param symptoms      the symptoms of the patient
     * @param paymentMethod the payment method of the patient
     * @param roomNumber    the room number assigned to the patient
     * @param emergency     whether the patient is an emergency case
     * @param doctor        the ID of the doctor in charge
     */
    public EmergencyPatient(int ID, String name, String address, String phoneNumber, String gender,
                            String symptoms, String paymentMethod, int roomNumber, boolean emergency, int doctor) {
        super(ID, name, address, phoneNumber, gender, symptoms, paymentMethod, emergency, doctor);
        setRoomNumber(roomNumber);
    }
}
