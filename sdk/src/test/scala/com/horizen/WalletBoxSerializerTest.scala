package com.horizen

import scorex.core.{NodeViewModifier, bytesToId, idToBytes}

import com.horizen.box.{Box, BoxSerializer}
import com.horizen.companion.SidechainBoxesCompanion
import com.horizen.customtypes.{CustomBox, CustomBoxSerializer}
import com.horizen.fixtures.BoxFixture
import com.horizen.proposition.Proposition
import com.horizen.utils.BytesUtils
import org.junit.{Before, Test}
import org.junit.Assert._
import org.scalatest.junit.JUnitSuite
import java.util.{HashMap => JHashMap}
import java.lang.{Byte => JByte}

import scala.util.Random

class WalletBoxSerializerTest extends JUnitSuite with BoxFixture {
  @Test
  def WalletBoxSerializerTest_SerializationTest(): Unit = {
    val transactionIdBytes = new Array[Byte](32)
    var customBoxesSerializers: JHashMap[JByte, BoxSerializer[SidechainTypes#SCB]] = new JHashMap()
    customBoxesSerializers.put(CustomBox.BOX_TYPE_ID, CustomBoxSerializer.getSerializer.asInstanceOf[BoxSerializer[SidechainTypes#SCB]])
    val sidechainBoxesCompanion = SidechainBoxesCompanion(customBoxesSerializers)

    var serializer: WalletBoxSerializer = null
    var bytes: Array[Byte] = null

    // Test 1: serialization for core Box
    Random.nextBytes(transactionIdBytes)
    val walletBoxWithRegularBox = new WalletBox(
      getRegularBox(getSecret("seed1".getBytes), 1, 100),
      bytesToId(transactionIdBytes),
      10000)
    serializer = walletBoxWithRegularBox.serializer(sidechainBoxesCompanion)
    bytes = serializer.toBytes(walletBoxWithRegularBox)
    val parsedWalletBoxWithRegularBox = serializer.parseBytesTry(bytes).get
    assertEquals("Core WalletBoxes expected to be equal.", walletBoxWithRegularBox, parsedWalletBoxWithRegularBox)


    // Test 2: serialization of custom Box
    Random.nextBytes(transactionIdBytes)
    val walletBoxWithCustomBox = new WalletBox(
      getCustomBox().asInstanceOf[SidechainTypes#SCB],
      bytesToId(transactionIdBytes),
      20000)
    serializer = walletBoxWithCustomBox.serializer(sidechainBoxesCompanion)
    bytes = serializer.toBytes(walletBoxWithCustomBox)
    val parsedWalletBoxWithCustomBox = serializer.parseBytesTry(bytes).get
    assertEquals("Custom WalletBoxes expected to be equal.", walletBoxWithCustomBox, parsedWalletBoxWithCustomBox)


    // Test 3: try parse of broken bytes
    val failureExpected: Boolean = new WalletBoxSerializer(sidechainBoxesCompanion).parseBytesTry("broken bytes".getBytes).isFailure
    assertEquals("Failure during parsing expected.", true, failureExpected)

  }
}
