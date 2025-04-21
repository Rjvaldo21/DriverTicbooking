package com.app.driverticbooking;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {
    private static final String PREF_NAME = "DriverTicBookingSession";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "full_name";
    public static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";
    private static final String KEY_DEPARTEMENT = "departement";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public void saveUserData(String userJson) {
        try {
            JSONObject jsonObject = new JSONObject(userJson);
            editor.putString(KEY_EMAIL, jsonObject.optString("email", "La iha Email"));
            editor.apply();
            Log.d("SessionManager", "✅ Email salva ona: " + jsonObject.optString("email"));
        } catch (JSONException e) {
            Log.e("SessionManager", "❌ Erru parsing JSON: " + e.getMessage());
        }
    }

    public void saveEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            editor.putString(KEY_EMAIL, email.trim());
            editor.apply();
            Log.d("SessionManager", "✅ Email salva ona: " + email);
        } else {
            Log.e("SessionManager", "❌ Falha salva email, valor mamuk!");
        }
    }

    public void saveDepartement(String departement) {
        editor.putString("departement", departement);
        editor.apply();
    }

    public void saveRole(String role) {
        editor.putString("role", role);
        editor.apply();
    }

    public void saveVehicleName(String name) {
        editor.putString("vehicle_name", name);
        editor.apply();
    }

    public void saveVehicleType(String type) {
        editor.putString("vehicle_type", type);
        editor.apply();
    }

    public void saveVehicleStatus(String status) {
        editor.putString("vehicle_status", status);
        editor.apply();
    }

    public void saveVehicleCapacity(int capacity) {
        editor.putInt("vehicle_capacity", capacity);
        editor.apply();
    }


    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", "Token " + token.trim());
        Log.d("DEBUG TOKEN", "Token nebe uza: [" + token + "]");
        editor.apply();
    }

    public void saveUserName(String userName) {
        if (userName != null && !userName.trim().isEmpty()) {
            editor.putString(KEY_USERNAME, userName);
            editor.apply();
            Log.d("SessionManager", "👤 Username salava ona: " + userName);
        } else {
            Log.e("SessionManager", "❌ Username mamuk, la salva!");
        }
    }

    public void logout() {
        editor.clear();
        editor.commit();
        Log.d("SessionManager", "🔴 User logged out no session hamos ona.");
    }

    public int getUserId() {
        int userId = sharedPreferences.getInt(KEY_USER_ID, -1);
        Log.d("SessionManager", "📌 User ID: " + userId);
        return userId;
    }

    public String getUserName() {
        String userName = sharedPreferences.getString(KEY_USERNAME, "La iha naran");
        Log.d("SessionManager", "📌 Username: " + userName);
        return userName;
    }

    public String getFullName() {
        String fullName = sharedPreferences.getString(KEY_FULL_NAME, "La iha naran kompletu");
        Log.d("SessionManager", "📌 Full Name: " + fullName);
        return fullName;
    }

    public String getEmail() {
        String email = sharedPreferences.getString(KEY_EMAIL, "La iha email");
        Log.d("SessionManager", "📌 Email husi SharedPreferences: " + email);
        return email;
    }

    public String getRole() {
        String role = sharedPreferences.getString(KEY_ROLE, "La iha Role");
        Log.d("SessionManager", "📌 Role: " + role);
        return role;
    }

    public String getDepartement() {
        String departement = sharedPreferences.getString(KEY_DEPARTEMENT, "La iha departamentu");
        Log.d("SessionManager", "📌 Departamentu: " + departement);
        return departement;
    }

    public boolean isLoggedIn() {
        boolean loggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
        Log.d("SessionManager", "📌 User login ona? " + loggedIn);
        return loggedIn;
    }

    public String getToken() {
        String token = sharedPreferences.getString("auth_token", "");
        Log.d("DEBUG TOKEN", "Token nebe uza: [" + token + "]");
        return token;
    }
}
