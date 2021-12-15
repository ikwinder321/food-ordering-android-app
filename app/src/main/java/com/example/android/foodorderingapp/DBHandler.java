package com.example.android.foodorderingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "Twiggy";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "foodorderuser";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our course name column
    private static final String NAME_COL = "name";

    // below variable id for our course duration column.
    private static final String NUMBER = "number";

    // below variable for our course description column.
    private static final String EMAIL = "email";

    // below variable is for our course tracks column.
    private static final String PINCODE = "pincode";

    private static final String ADDRESS = "address";

    private static final String PASSWORD = "password";

    private static final String FOOD_TABLE = "food";
    private static final String FOOD_TYPE = "type";
    private static final String FOOD_PRICE = "price";
    private static final String FOOD_NAME = "name";
    private static final String FOOD_ID = "id";

    private static final String ORDERS_TABLE = "orders";
    private static final String ORDER_ID = "ID";
    private static final String ORDER_DATE = "Date";
    private static final String ORDER_PRICE = "Price";
    private static final String ORDER_EMAIL = "Email";


    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + NUMBER + " TEXT,"
                + EMAIL + " TEXT,"
                + PINCODE + " TEXT,"
                + ADDRESS + " TEXT,"
                + PASSWORD + " TEXT)";
        db.execSQL(query);

        query = "CREATE TABLE " + FOOD_TABLE + " (" +
                FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FOOD_NAME + " TEXT,"
                + FOOD_TYPE + " TEXT,"
                + FOOD_PRICE + " INT)";
        db.execSQL(query);

        query = "CREATE TABLE " + ORDERS_TABLE + " (" +
                ORDER_ID + " LONG,"
                + ORDER_DATE + " TEXT,"
                + ORDER_PRICE + " INT,"
                + ORDER_EMAIL + " TEXT)";
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void addNewUser(String username, String usernumber, String email, String pincode, String address, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, username);
        values.put(NUMBER, usernumber);
        values.put(EMAIL, email);
        values.put(PINCODE, pincode);
        values.put(ADDRESS, address);
        values.put(PASSWORD, password);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void addNewOrder(long id, String date, int price, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDER_ID, id);
        values.put(ORDER_DATE, date);
        values.put(ORDER_PRICE, price);
        values.put(ORDER_EMAIL, email);
        db.insert(ORDERS_TABLE, null, values);
        db.close();
    }

    public void addDishes() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FOOD_NAME, "American Fresh");
        values.put(FOOD_PRICE, 7);
        values.put(FOOD_TYPE, "breakfast");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Mexican Duoto");
        values.put(FOOD_PRICE, 5);
        values.put(FOOD_TYPE, "breakfast");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "German Pie");
        values.put(FOOD_PRICE, 11);
        values.put(FOOD_TYPE, "breakfast");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Australian Toasts");
        values.put(FOOD_PRICE, 15);
        values.put(FOOD_TYPE, "breakfast");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Russian Beans");
        values.put(FOOD_PRICE, 4);
        values.put(FOOD_TYPE, "breakfast");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Indian Omelet");
        values.put(FOOD_PRICE, 6);
        values.put(FOOD_TYPE, "breakfast");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Roti Roles");
        values.put(FOOD_PRICE, 18);
        values.put(FOOD_TYPE, "lunch");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Burger & Fries");
        values.put(FOOD_PRICE, 11);
        values.put(FOOD_TYPE, "lunch");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Sub Bread");
        values.put(FOOD_PRICE, 9);
        values.put(FOOD_TYPE, "lunch");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Aalo Poori");
        values.put(FOOD_PRICE, 3);
        values.put(FOOD_TYPE, "lunch");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Salad");
        values.put(FOOD_PRICE, 16);
        values.put(FOOD_TYPE, "lunch");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Corn Soup");
        values.put(FOOD_PRICE, 18);
        values.put(FOOD_TYPE, "lunch");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Bean Onions");
        values.put(FOOD_PRICE, 22);
        values.put(FOOD_TYPE, "dinner");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Paneer Crisps");
        values.put(FOOD_PRICE, 14);
        values.put(FOOD_TYPE, "dinner");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Mexican Fato");
        values.put(FOOD_PRICE, 25);
        values.put(FOOD_TYPE, "dinner");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Soya Yato");
        values.put(FOOD_PRICE, 8);
        values.put(FOOD_TYPE, "dinner");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Egg Chunks");
        values.put(FOOD_PRICE, 12);
        values.put(FOOD_TYPE, "dinner");
        db.insert(FOOD_TABLE, null, values);
        values.clear();

        values.put(FOOD_NAME, "Potato Balls");
        values.put(FOOD_PRICE, 11);
        values.put(FOOD_TYPE, "dinner");
        db.insert(FOOD_TABLE, null, values);

        db.close();
    }

    public ArrayList<UserModal> readUserData(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorUser = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + EMAIL + " = \"" + userEmail + "\"", null);
        ArrayList<UserModal> userModalList = new ArrayList<>();
        if (cursorUser.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                userModalList.add(new UserModal(cursorUser.getString(1),
                        cursorUser.getString(2),
                        cursorUser.getString(3),
                        cursorUser.getString(4),
                        cursorUser.getString(5),
                        cursorUser.getString(6)));
            } while (cursorUser.moveToNext());
            // moving our cursor to next.
        }
        cursorUser.close();
        return userModalList;
    }

    public ArrayList<FoodModal> readFoodData(String foodType) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorFood = db.rawQuery("SELECT * FROM " + FOOD_TABLE + " WHERE " + FOOD_TYPE + " = \"" + foodType + "\" GROUP BY " + FOOD_NAME, null);
        ArrayList<FoodModal> foodModalArrayList = new ArrayList<>();
        if (cursorFood.moveToFirst()) {
            do {
                foodModalArrayList.add(new FoodModal(cursorFood.getString(1),
                        cursorFood.getString(2),
                        cursorFood.getInt(3)));
            } while (cursorFood.moveToNext());
        }
        cursorFood.close();
        return foodModalArrayList;
    }

    public ArrayList<OrderModal> readOrdersData(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorOrder = db.rawQuery("SELECT * FROM " + ORDERS_TABLE + " WHERE " + ORDER_EMAIL + " = \"" + email + "\" ORDER BY " + ORDER_ID + " DESC", null);
        ArrayList<OrderModal> orderModalArrayList = new ArrayList<>();
        if (cursorOrder.moveToFirst()) {
            do {
                orderModalArrayList.add(new OrderModal(cursorOrder.getLong(0), cursorOrder.getString(1), cursorOrder.getInt(2), cursorOrder.getString(3)));
            } while (cursorOrder.moveToNext());
        }
        cursorOrder.close();
        return orderModalArrayList;
    }

    public void updateUserTable(String userEmail, String pinCode, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PINCODE, pinCode);
        values.put(ADDRESS, address);
        db.update(TABLE_NAME, values, "email=?", new String[]{userEmail});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

