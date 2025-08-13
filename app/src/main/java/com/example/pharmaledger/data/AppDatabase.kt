package com.example.pharmaledger.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pharmaledger.data.dao.ExpenseDao
import com.example.pharmaledger.data.dao.MedicineDao
import com.example.pharmaledger.data.dao.SaleDao
import com.example.pharmaledger.data.entity.ExpenseEntity
import com.example.pharmaledger.data.entity.MedicineEntity
import com.example.pharmaledger.data.entity.SaleEntity

@Database(
	entities = [MedicineEntity::class, SaleEntity::class, ExpenseEntity::class],
	version = 1,
	exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
	abstract fun medicineDao(): MedicineDao
	abstract fun saleDao(): SaleDao
	abstract fun expenseDao(): ExpenseDao

	companion object {
		@Volatile
		private var INSTANCE: AppDatabase? = null

		fun getInstance(context: Context): AppDatabase {
			return INSTANCE ?: synchronized(this) {
				INSTANCE ?: Room.databaseBuilder(
					context.applicationContext,
					AppDatabase::class.java,
					"pharmaledger.db"
				).fallbackToDestructiveMigration().build().also { INSTANCE = it }
			}
		}
	}
}