package com.example.yon.project60;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Yon on 1/2/2560.
 */

public class BackgroundTask extends AsyncTask<String, Void, String> {
    Context ctx;
    AlertDialog alertDialog;
    SharedPreferences sharedpreferences;

    BackgroundTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Login Status");
        sharedpreferences = ctx.getSharedPreferences("Tooyen", Context.MODE_PRIVATE);
        //sharedpreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    @Override
    protected String doInBackground(String... params) {
        String host_ip = "10.105.27.105";
        String reg_url = "http://" + host_ip + "/webapp/register.php";
        String login_url = "http://" + host_ip + "/webapp/login.php";
        String Add_url = "http://" + host_ip + "/webapp/fresh_list.php";
        String creategroup_url = "http://" + host_ip + "/webapp/create_group.php";
        String joingroup_url = "http://" + host_ip + "/webapp/join_group.php";
        String leave_group_url = "http://" + host_ip + "/webapp/leave_group.php";
        String fresh_delete_url = "http://" + host_ip + "/webapp/fresh_list_delete.php";
        String rectify_list_url = "http://" + host_ip + "/webapp/rectify_list.php";
        String shopping_list_url = "http://" + host_ip + "/webapp/shopping_list.php";
        String shopping_delete_url = "http://" + host_ip + "/webapp/shopping_list_delete.php";
        String shopping_update_url = "http://" + host_ip + "/webapp/shopping_list_update.php";

        String type = params[0];


        //--------------------------------------- register ---------------------------------------//
        if (type.equals("register")) {
            String name = params[1];
            String user_name = params[2];
            String user_pass = params[3];
            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&" +
                        URLEncoder.encode("user_pass", "UTF-8") + "=" + URLEncoder.encode(user_pass, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //----------------------------------------------------Login--------------------------------------------------------
        else if (type.equals("login")) {
            String username = params[1];
            String password = params[2];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //----------------------------------------------------Addmenu--------------------------------------------------------
        else if (type.equals("addMenu")) {
            String Fresh_Name = params[1];
            String Amount = params[2];
            String S_Unit = params[3];
            String S_Type_name = params[4];
            String Exp = params[5];
            String ba1 = params[6];
            String User_id = params[7];
            String Calorie = params[8];
            String join_leave_id  = params[9];
            String group_id = params[10];
            String shop_id = params[11];

            String ImageName = System.currentTimeMillis() + ".jpg";

            try {
                URL url = new URL(Add_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("fresh_name", "UTF-8") + "=" + URLEncoder.encode(Fresh_Name, "UTF-8") + "&"
                        + URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(Amount, "UTF-8") + "&"
                        + URLEncoder.encode("unit", "UTF-8") + "=" + URLEncoder.encode(S_Unit, "UTF-8") + "&"
                        + URLEncoder.encode("type_name", "UTF-8") + "=" + URLEncoder.encode(S_Type_name, "UTF-8") + "&"
                        + URLEncoder.encode("exp", "UTF-8") + "=" + URLEncoder.encode(Exp, "UTF-8") + "&"
                        + URLEncoder.encode("picture", "UTF-8") + "=" + URLEncoder.encode(ba1, "UTF-8") + "&"
                        + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(User_id, "UTF-8") + "&"
                        + URLEncoder.encode("calorie", "UTF-8") + "=" + URLEncoder.encode(Calorie, "UTF-8") + "&"
                        + URLEncoder.encode("join_leave_id", "UTF-8") + "=" + URLEncoder.encode(join_leave_id, "UTF-8") + "&"
                        + URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode(group_id, "UTF-8") + "&"
                        + URLEncoder.encode("shop_id", "UTF-8") + "=" + URLEncoder.encode(shop_id, "UTF-8") + "&"
                        + URLEncoder.encode("imagename", "UTF-8") + "=" + URLEncoder.encode(ImageName, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //---------------------------------------CreateGroup-----------------------------------------------
        else if (type.equals("creategroup")) {
            String groupname = params[1];
            String grouppass = params[2];
            String user_name = params[3];
            try {
                URL url = new URL(creategroup_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("groupname", "UTF-8") + "=" + URLEncoder.encode(groupname, "UTF-8") + "&" +
                        URLEncoder.encode("grouppass", "UTF-8") + "=" + URLEncoder.encode(grouppass, "UTF-8") + "&" +
                        URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //----------------------------------------------------JoinGroup--------------------------------------------------------
        else if (type.equals("joingroup")) {
            String groupname = params[1];
            String grouppass = params[2];
            String user_id = params[3];

            try {
                URL url = new URL(joingroup_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("groupname", "UTF-8") + "=" + URLEncoder.encode(groupname, "UTF-8") + "&" +
                        URLEncoder.encode("grouppass", "UTF-8") + "=" + URLEncoder.encode(grouppass, "UTF-8") + "&" +
                        URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //----------------------------------------------------LeaveGroup--------------------------------------------------------
        else if (type.equals("leavegroup")) {
            String user_id = params[1];
            String group_id = params[2];

            try {
                URL url = new URL(leave_group_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                        + URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode(group_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //----------------------------------------------------DeleteFreshList--------------------------------------------------------
        else if (type.equals("DelFreshList")) {
            String user_id = params[1];
            String group_id = params[2];
            String valuefresh = params[3];

            try {
                URL url = new URL(fresh_delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                        + URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode(group_id, "UTF-8") + "&"
                        + URLEncoder.encode("valuefresh", "UTF-8") + "=" + URLEncoder.encode(valuefresh, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //----------------------------------------------------Rectify--------------------------------------------------------
        else if (type.equals("rectify")) {
            String user_id = params[1];
            String group_id = params[2];
            String valuefresh = params[3];
            String Fresh_Name = params[4];
            String Amount = params[5];
            String S_Unit = params[6];
            String S_Type_name = params[7];
            String Exp = params[8];
            String Calorie = params[9];


            try {
                URL url = new URL(rectify_list_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                        + URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode(group_id, "UTF-8") + "&"
                        + URLEncoder.encode("valuefresh", "UTF-8") + "=" + URLEncoder.encode(valuefresh, "UTF-8") + "&"
                        + URLEncoder.encode("Fresh_Name", "UTF-8") + "=" + URLEncoder.encode(Fresh_Name, "UTF-8") + "&"
                        + URLEncoder.encode("Amount", "UTF-8") + "=" + URLEncoder.encode(Amount, "UTF-8") + "&"
                        + URLEncoder.encode("S_Unit", "UTF-8") + "=" + URLEncoder.encode(S_Unit, "UTF-8") + "&"
                        + URLEncoder.encode("S_Type_name", "UTF-8") + "=" + URLEncoder.encode(S_Type_name, "UTF-8") + "&"
                        + URLEncoder.encode("Exp", "UTF-8") + "=" + URLEncoder.encode(Exp, "UTF-8") + "&"
                        + URLEncoder.encode("Calorie", "UTF-8") + "=" + URLEncoder.encode(Calorie, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //----------------------------------------------------Shoppinglist--------------------------------------------------------
        else if (type.equals("shoppinglist")) {
            String namelist = params[1];
            String status = params[2];
            String user_id = params[3];
            String group_id = params[4];

            try {
                URL url = new URL(shopping_list_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("namelist", "UTF-8") + "=" + URLEncoder.encode(namelist, "UTF-8") + "&"
                        + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8") + "&"
                        + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                        + URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode(group_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //----------------------------------------------------DeleteShoppinglist--------------------------------------------------------
        else if (type.equals("DelShoppingList")) {
            String user_id = params[1];
            String group_id = params[2];
            String valueShop_id = params[3];

            try {
                URL url = new URL(shopping_delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                        + URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode(group_id, "UTF-8") + "&"
                        + URLEncoder.encode("valueShop_id", "UTF-8") + "=" + URLEncoder.encode(valueShop_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //----------------------------------------------------UpdateStatusShoppinglist--------------------------------------------------------
        else if (type.equals("updatestatus")) {
            String user_id = params[1];
            String group_id = params[2];
            String valueShop_id = params[3];
            String statusvalue = params[4];

            try {
                URL url = new URL(shopping_update_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                        + URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode(group_id, "UTF-8") + "&"
                        + URLEncoder.encode("valueShop_id", "UTF-8") + "=" + URLEncoder.encode(valueShop_id, "UTF-8") + "&"
                        + URLEncoder.encode("statusvalue", "UTF-8") + "=" + URLEncoder.encode(statusvalue, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("สมัครสมาชิกสำเร็จ")) { //---Register
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            ((Activity) ctx).finish();
            return;
        } else if (result.equals("ชื่อผู้ใช้นี้มีผู้อื่นใช้แล้ว ลองใช้ชื่ออื่น")) { //---Register false
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        } else if (result.equals("กลุ่มผู้ใช้นี้มีผู้อื่นใช้แล้ว ลองใช้ชื่ออื่น")) { //---CreateGroup false
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        } else if (result.equals("สร้างกลุ่มสำเร็จ")) { //---CreateGroup
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            ((Activity) ctx).finish();
        } else if (result.equals("เข้าร่วมกลุ่มสำเร็จ")) { //---JoinGroup
            alertDialog.setMessage(result);
            alertDialog.show();

            Intent intent = new Intent(ctx, home_all.class);
            ctx.startActivity(intent);
            ((Activity) ctx).finish();
        } else if (result.equals("ชื่อกลุ่มหรือรหัสผ่านผิดกรุณาลองใหม่อีกครั้ง")) {//---JoinGroup
            alertDialog.setMessage(result);
            alertDialog.show();
        } else if (result.equals("คุณเข้าร่วมกลุ่มนี้อยู่แล้ว")) {//---JoinGroup
            alertDialog.setMessage(result);
            alertDialog.show();
        } else if (result.equals("ไม่สามารถเข้าร่วมกลุ่มได้")) {//---JoinGroup
            alertDialog.setMessage(result);
            alertDialog.show();
        } else if (result.equals("ออกจากกลุ่มสำเร็จ")) {//---LeaveGroup
            alertDialog.setMessage(result);
            alertDialog.setTitle("LeaveGroup Status");
            alertDialog.show();
        } else if (result.equals("ออกจากกลุ่มไม่สำเร็จ")) {//---LeaveGroup
            alertDialog.setMessage(result);
            alertDialog.show();
        } else if (result.equals("ลบสำเร็จ")) {//---DeleteFreshList

        }  else if (result.equals("ลบไม่สำเร็จ")) {//---DeleteFreshList
            alertDialog.setMessage(result);
            alertDialog.show();
        } else if (result.equals("แก้ไขสำเร็จ")) {//---rectify
            Intent intent = new Intent(ctx, home_all.class);
            ctx.startActivity(intent);
            ((Activity) ctx).finish();
        }  else if (result.equals("แก้ไขไม่สำเร็จ")) {//---rectify
            alertDialog.setMessage(result);
            alertDialog.show();
        }  else if (result.equals("สำเร็จ")) {//---shoppinglist
            Toast.makeText(ctx, "เพิ่มรายการชอปปิงลิสต์สำเร็จ", Toast.LENGTH_LONG).show();
        }  else if (result.equals("ไม่สำเร็จ")) {//---shoppinglist
            alertDialog.setMessage(result);
            alertDialog.show();
        } else if (result.equals("ลบชอปปิงลิสต์สำเร็จ")) {//---Deleteshoppinglist

        }  else if (result.equals("ลบชอปปิงลิสต์ไม่สำเร็จ")) {//---Deleteshoppinglist
            alertDialog.setMessage(result);
            alertDialog.show();
        } else if (result.equals("อัพเดทสำเร็จ")) {//---UpdateStatusShoppinglist

        }  else if (result.equals("อัพเดทไม่สำเร็จ")) {//---UpdateStatusShoppinglist
            alertDialog.setMessage(result);
            alertDialog.show();
        } else if (result.equals("addMenu Success....")) { //---AddMenu
            alertDialog.setMessage(result);
            alertDialog.setTitle("AddMenu Status");
            alertDialog.show();

        } else if (result.equals("login 0")) { //---Login False
            alertDialog.setMessage(result);
            alertDialog.show();

        } else { //---Login Success
            String[] getid = result.split(" ");
            String id = getid[1];
            String name = getid[2];

            alertDialog.setMessage(getid[0] + getid[1]);
            alertDialog.show();

            Intent intent = new Intent(ctx, home_all.class);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("user_id", id);
            editor.putString("user_name", name);
            editor.commit();
            ctx.startActivity(intent);
            ((Activity) ctx).finish();

        }


    }


}
