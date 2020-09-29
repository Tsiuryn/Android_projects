package com.example.helper.ui.calculator

import android.app.AlertDialog
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helper.R
import com.example.helper.ui.calculator.my_database.database.CalculationsRepository
import com.example.helper.ui.calculator.my_database.local.CalculationsDataSource
import com.example.helper.ui.calculator.my_database.local.CalculationsDatabase
import com.example.helper.ui.calculator.my_database.model.Calculations
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class DatabaseCalculatorFragment : Fragment() {
    private lateinit var adapter: RecyclerAdapter
    private var listCalculations = ArrayList<Calculations>()
    private lateinit var compositeDisposable: CompositeDisposable


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_database_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        compositeDisposable = CompositeDisposable()
        val recyclerView = view.findViewById<RecyclerView>(R.id.listCalculations)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = RecyclerAdapter(listCalculations)
        recyclerView.adapter = adapter
        getFromDatabase()
        itemTouchHelper(recyclerView)
    }

    //Свайп для items адаптера
    private fun itemTouchHelper (recyclerView: RecyclerView){
        ItemTouchHelper(object : SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val calculations = listCalculations[viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition
                deleteItemDialog(calculations, position+1)
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }


        }).attachToRecyclerView(recyclerView)
    }

    //Нажатие на кнопку удаления в меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteBase -> alertForDeleteAllBase()
        }
        return super.onOptionsItemSelected(item)
    }

    //Смена кнопки в меню
    override fun onStop() {
        super.onStop()
        CalcFragment.isDbFragment = 1
    }

    //диалог при удалении всей БД
    private fun alertForDeleteAllBase() {
        if (listCalculations.size > 0) {
            val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
                    .setTitle("")
                    .setCancelable(false)
                    .setMessage("Вы хотите очистить полностью БД?")
                    .setPositiveButton("OK", null)
                    .setNegativeButton("CANCEL") { dialog, which -> dialog.dismiss() }
            val alertDialog = builder.create()
            alertDialog.setOnShowListener { dialog ->
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnLongClickListener {
                    deleteDataBase()
                    alertDialog!!.dismiss()
                    true
                }
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    Toast.makeText(context, "Необходимо длинное нажатие!", Toast.LENGTH_SHORT).show()
                    alertDialog.show()
                }
            }
            alertDialog.show()
        }
    }

    //удаление базы данных
    private fun deleteDataBase() {
        val db = CalculationsDatabase.getInstance(context)
        val repo = CalculationsRepository.getInstance(CalculationsDataSource.getInstance(db.calculationsDAO()))
        val disposable = Observable.create<Any> { e ->
            repo.deleteAllCalculations()
            getFromDatabase()
            e.onComplete()
        }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { Toast.makeText(context, "User added !", Toast.LENGTH_SHORT).show() }
        compositeDisposable.add(disposable)
    }

    //заполнение list из БД
    private fun getFromDatabase() {
        val db = CalculationsDatabase.getInstance(context)
        val repo = CalculationsRepository.getInstance(CalculationsDataSource.getInstance(db.calculationsDAO()))
        val disposable = repo.allCalculations
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Consumer<List<Calculations>> {
                    override fun accept(t: List<Calculations>?) {
                        if (t != null) {
                            Log.d("TAG", "${t.size} = db")
                            listCalculations.clear()
                            listCalculations.addAll(t)
                            Log.d("TAG", "${listCalculations.size} = list")
                            listCalculations.reverse()
                            adapter.updateAdapter(listCalculations)
                        }
                    }
                })
        compositeDisposable.add(disposable)
    }

    //создание класса адаптера
    inner class RecyclerAdapter(private var itemList: ArrayList<Calculations>) : RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
            return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_database_calculator, parent, false))
        }

        override fun getItemCount(): Int = itemList.size
        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            val calc = itemList[position]
            holder.calc.setText(calc.edit)
            holder.value.setText("= ${calc.result}")
            holder.date.setText(calc.date)
            holder.id.setText("${position + 1}.")
            holder.descr.setText(getDescription(calc))
            holder.onItemClickListener(position, calc)
        }

        private fun getDescription (calculations: Calculations): String{
            if (calculations.description.isNotEmpty()){
                return calculations.description
            }
            else return "Нет описания!"
        }

        fun updateAdapter(list: List<Calculations>) {
            itemList = list as ArrayList<Calculations>
            notifyDataSetChanged()
        }

        inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val calc = itemView.findViewById<TextView>(R.id.calculation)
            val value = itemView.findViewById<TextView>(R.id.value)
            val date = itemView.findViewById<TextView>(R.id.date)
            val id = itemView.findViewById<TextView>(R.id.myId)
            val descr = itemView.findViewById<TextView>(R.id.description_db)


            fun onItemClickListener(position: Int, calculations: Calculations) {
                itemView.setOnLongClickListener {
                    dialogItemOptions(position, calculations)

                    true
                }
            }
        }
    }

    //диалог - опция item
    private fun dialogItemOptions(position: Int, calculations: Calculations) {
        val view: View = layoutInflater.inflate(R.layout.custom_alert_dialog_db, null)
        val alertDialog = AlertDialog.Builder(context)
                .setTitle("")
                .setView(view)
                .create()
        val name = view.findViewById<TextView>(R.id.alertName)
        name.text = "ОПЦИЯ ПОЛЯ №${position + 1}"

        val desc = view.findViewById<TextView>(R.id.alertDescription)
        desc.setOnClickListener(View.OnClickListener {
            dialogDescription(calculations)
            Toast.makeText(context, "Добавьте описание!", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        })

        val value = view.findViewById<TextView>(R.id.getAlertValue)
        value.setOnClickListener(View.OnClickListener {
            Toast.makeText(context, "Выражение добавлено!", Toast.LENGTH_SHORT).show()
            pushToCalculator(calculations.edit)
            alertDialog.dismiss()
        })
        alertDialog.show()

    }

    //диалог- для удаления item
    private fun deleteItemDialog (calculations: Calculations, position: Int){
        AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setMessage("Удалить выражение №$position")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, which ->
                    deleteItemFromDatabase(calculations)
                    dialog.dismiss()
                }
                .setNegativeButton("CANCEL") { dialog, which ->
                    adapter.updateAdapter(listCalculations)
                    dialog.dismiss()}
                .create()
                .show()
    }

    //диалог для создания описания item
    private fun dialogDescription(calculations: Calculations) {
        val text = EditText(context)
        val calc = calculations
        text.setText(calculations.description)
//        text.setTextColor(resources.getColor(R.color.textColor))
        text.hint = "ввод значения"
        val alertDialog = AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setMessage("Введите описание: ")
                .setView(text)
                .setCancelable(false)
                .setPositiveButton("Ok") { dialog, which ->
                    run {
                        if (text.text.isNotEmpty()) {
                            calc.description = text.text.toString()
                            updateDatabase(calc)

                        }

                    }
                }.setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
        alertDialog.create().show()
    }

    //обновление строки в БД после добавления описания
    private fun updateDatabase(calculations: Calculations) {
        val db = CalculationsDatabase.getInstance(context)
        val repo = CalculationsRepository
                .getInstance(CalculationsDataSource
                        .getInstance(db.calculationsDAO()))
        val disposable = io.reactivex.Observable.create(object : ObservableOnSubscribe<Object> {
            override fun subscribe(emitter: ObservableEmitter<Object>) {
                repo.updateCalculations(calculations)
                emitter.onComplete()
            }

        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(Consumer<Object> { }, Consumer<Throwable> { }, Action { getFromDatabase() })
        compositeDisposable.add(disposable)
    }

    //удаление строки из БД
    private fun deleteItemFromDatabase (calculations: Calculations){
        val db = CalculationsDatabase.getInstance(context)
        val repo = CalculationsRepository
                .getInstance(CalculationsDataSource
                        .getInstance(db.calculationsDAO()))
        val disposable = io.reactivex.Observable.create(object : ObservableOnSubscribe<Object> {
            override fun subscribe(emitter: ObservableEmitter<Object>) {
                repo.deleteCalculations(calculations)
                emitter.onComplete()

            }

        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(Consumer<Object> { }, Consumer<Throwable> { }, Action { getFromDatabase() })
        compositeDisposable.add(disposable)
    }

    //взять выражение для калькулятора
    private fun pushToCalculator (value: String){
        clearFragmentBackStack()
        val bundle = Bundle()
        bundle.putString("KEY", value)
        val calculator = CalcFragment()
        calculator.arguments = bundle
        activity!!.supportFragmentManager
                .beginTransaction().replace(R.id.nav_host_fragment, calculator).commit()

    }

    //очистка стэка
    private fun clearFragmentBackStack (){
        val fm = activity?.supportFragmentManager
        for (i in 0 until fm!!.backStackEntryCount){
            fm.popBackStack()
        }
    }

    //отвязываемся от compositeDisposable
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
