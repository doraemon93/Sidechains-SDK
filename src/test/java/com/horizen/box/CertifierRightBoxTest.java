package com.horizen.box;

import com.horizen.proposition.PublicKey25519Proposition;
import org.junit.Before;
import org.junit.Test;
import scala.Tuple2;
import scorex.crypto.signatures.Curve25519;

import java.util.Arrays;

import static org.junit.Assert.*;

public class CertifierRightBoxTest
{
    PublicKey25519Proposition proposition;
    long nonce;
    long minimumWithdrawalEpoch;

    @Before
    public void setUp() {
        byte[] anotherSeed = "certrighttestseed".getBytes();
        Tuple2<byte[], byte[]> keyPair = Curve25519.createKeyPair(anotherSeed);
        proposition = new PublicKey25519Proposition(keyPair._2());

        nonce = 12345;
        minimumWithdrawalEpoch = 5;
    }

    @Test
    public void CertifierRightBox_CreationTest() {
        CertifierRightBox box = new CertifierRightBox(proposition, nonce, minimumWithdrawalEpoch);

        assertEquals("CertifierRightBox creation: proposition is wrong", true, box.proposition() == proposition);
        assertEquals("CertifierRightBox creation: nonce is wrong", true, box.nonce() == nonce);
        assertEquals("CertifierRightBox creation: activeFromWithdrawalEpoch is wrong", true, box.activeFromWithdrawalEpoch() == minimumWithdrawalEpoch);
        assertEquals("CertifierRightBox creation: value is wrong", true, box.value() == 0);
    }

    @Test
    public void CertifierRightBox_ComparisonTest() {
        CertifierRightBox box1 = new CertifierRightBox(proposition, nonce, minimumWithdrawalEpoch);
        CertifierRightBox box2 = new CertifierRightBox(proposition, nonce, minimumWithdrawalEpoch);

        assertEquals("Boxes hash codes expected to be equal", true, box1.hashCode() == box2.hashCode());
        assertEquals("Boxes expected to be equal", true, box1.equals(box2));
        assertEquals("Boxes ids expected to be equal", true, Arrays.equals(box1.id(), box2.id()));


        byte[] anotherSeed = "another test seed".getBytes();
        Tuple2<byte[], byte[]> keyPair = Curve25519.createKeyPair(anotherSeed);
        PublicKey25519Proposition anotherProposition = new PublicKey25519Proposition(keyPair._2());

        CertifierRightBox box3 = new CertifierRightBox(anotherProposition, nonce, minimumWithdrawalEpoch);
        assertEquals("Boxes hash codes expected to be different", false, box1.hashCode() == box3.hashCode());
        assertEquals("Boxes expected to be different", false, box1.equals(box3));
        assertEquals("Boxes ids expected to be different", false, Arrays.equals(box1.id(), box3.id()));


        CertifierRightBox box4 = new CertifierRightBox(proposition, nonce + 1, minimumWithdrawalEpoch);
        assertEquals("Boxes hash codes expected to be equal", true, box1.hashCode() == box4.hashCode());
        assertEquals("Boxes expected to be different", false, box1.equals(box4));
        assertEquals("Boxes ids expected to be different", false, Arrays.equals(box1.id(), box4.id()));

        CertifierRightBox box5 = new CertifierRightBox(proposition, nonce, minimumWithdrawalEpoch + 1);
        assertEquals("Boxes hash codes expected to be equal", true, box1.hashCode() == box5.hashCode());
        assertEquals("Boxes expected to be different", false, box1.equals(box5));
        assertEquals("Boxes ids expected to be different", true, Arrays.equals(box1.id(), box5.id()));

    }
}