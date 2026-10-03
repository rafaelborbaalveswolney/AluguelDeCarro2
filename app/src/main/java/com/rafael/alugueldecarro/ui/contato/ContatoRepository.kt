package com.rafael.alugueldecarro.ui.contato

import android.content.ContentResolver
import android.provider.ContactsContract

class ContatoRepository(
    private val contentResolver: ContentResolver
) {

    fun buscarContatos(): List<Contato> {

        val contatos = mutableListOf<Contato>()

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {

            val idIndex = it.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
            )

            val nomeIndex = it.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )

            val telefoneIndex = it.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )

            while (it.moveToNext()) {

                val id = it.getLong(idIndex)
                val nome = it.getString(nomeIndex) ?: ""
                val telefone = it.getString(telefoneIndex) ?: ""

                contatos.add(
                    Contato(
                        id = id,
                        nome = nome,
                        telefone = telefone
                    )
                )
            }
        }

        return contatos
    }
}