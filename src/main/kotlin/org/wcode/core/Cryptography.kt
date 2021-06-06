package org.wcode.core

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Cryptography {

    private lateinit var encryptCipher: Cipher
    private lateinit var decryptCipher: Cipher
    private val encorder = Base64.getEncoder()
    private val decorder = Base64.getDecoder()

    fun initCipher() {
        encryptCipher = createCipher(Cipher.ENCRYPT_MODE)
        decryptCipher = createCipher(Cipher.DECRYPT_MODE)
    }

    private fun createCipher(opMode: Int): Cipher {
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val sk = SecretKeySpec(EnvironmentConfig.secret.toByteArray(Charsets.UTF_8), "AES")
        val iv = IvParameterSpec(EnvironmentConfig.secret.substring(0, 16).toByteArray(Charsets.UTF_8))
        c.init(opMode, sk, iv)
        return c
    }

    fun String.encrypt(): String {
        val encrypted = encryptCipher.doFinal(this.toByteArray(Charsets.UTF_8))
        return String(encorder.encode(encrypted))
    }

    fun String.decrypt(): String {
        val byteStr = decorder.decode(this.toByteArray(Charsets.UTF_8))
        return String(decryptCipher.doFinal(byteStr))
    }
}
