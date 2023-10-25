package com.example.listalumnos

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listalumnos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AlumnoAdapter
    private val data = ArrayList<Alumno>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        data.add(Alumno("Sofia Arellano", "20131902", "jmorfin@ucol.mx", "https://imagenpng.com/wp-content/uploads/2017/02/pokemon-hulu-pikach.jpg"))
        data.add(Alumno("Luis Antonio", "20112345", "jmorfin@ucol.mx", "https://i.pinimg.com/236x/e0/b8/3e/e0b83e84afe193922892917ddea28109.jpg"))
        data.add(Alumno("Juan Pedro", "20122345", "jmorfin@ucol.mx", "https://i.pinimg.com/736x/9f/6e/fa/9f6efa277ddcc1e8cfd059f2c560ee53--clipart-gratis-vector-clipart.jpg"))

        adapter = AlumnoAdapter(this, data)
        binding.recyclerview.adapter = adapter

        adapter.setOnItemClickListener(object: AlumnoAdapter.ClickListener {
            override fun onItemClick(view: View, position: Int) {
                itemOptionsMenu(position)
            }
        })

        binding.faButton.setOnClickListener {
            val intento1 = Intent(this, MainActivityNuevo::class.java)
            startActivity(intento1)
        }

        // Variable para recibir extras
        val parExtra = intent.extras
        val msje = parExtra?.getString("mensaje")
        val nombre = parExtra?.getString("nombre")
        val cuenta = parExtra?.getString("cuenta")
        val correo = parExtra?.getString("correo")
        val image = parExtra?.getString("image")

        // Comprobar si el mensaje es para un nuevo alumno
        if (msje == "nuevo") {
            val insertIndex: Int = data.count()
            data.add(insertIndex, Alumno("$nombre", "$cuenta", "$correo", "$image"))
            adapter.notifyItemInserted(insertIndex)
        }
    }

    private fun itemOptionsMenu(position: Int) {
        val viewHolder = binding.recyclerview.findViewHolderForAdapterPosition(position)
            ?: return // Si no hay viewHolder, salimos

        val popupMenu = PopupMenu(this, viewHolder.itemView.findViewById(R.id.textViewOptions))
        popupMenu.inflate(R.menu.options_menu)

        val intento2 = Intent(this, MainActivityNuevo::class.java)

        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId) {
                    R.id.borrar -> {
                        val tmpAlum = data[position]
                        data.remove(tmpAlum)
                        adapter.notifyDataSetChanged()
                        return true
                    }
                    R.id.editar -> {
                        val alumno = data[position]
                        intento2.putExtra("mensaje", "edit")
                        intento2.putExtra("nombre", "${alumno.nombre}")
                        intento2.putExtra("cuenta", "${alumno.cuenta}")
                        intento2.putExtra("correo", "${alumno.correo}")
                        intento2.putExtra("image", "${alumno.imagen}")
                        startActivity(intento2)
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }
}
