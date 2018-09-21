package com.semicolon.huna.Services;

import com.semicolon.huna.Models.AllGroceries_SubCategory;
import com.semicolon.huna.Models.ClientOrderModel;
import com.semicolon.huna.Models.Client_Notification_Model;
import com.semicolon.huna.Models.ContactsModel;
import com.semicolon.huna.Models.CountryModel;
import com.semicolon.huna.Models.Driver_Grocery_Notification_Model;
import com.semicolon.huna.Models.Driver_Grocery_OrderModel;
import com.semicolon.huna.Models.ItemsModel;
import com.semicolon.huna.Models.MessageModel;
import com.semicolon.huna.Models.MiniMarketDataModel;
import com.semicolon.huna.Models.ResponseModel;
import com.semicolon.huna.Models.RuleModel;
import com.semicolon.huna.Models.SuperMarketModel;
import com.semicolon.huna.Models.UnreadModel;
import com.semicolon.huna.Models.UserModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Service {

    @Multipart
    @POST("Api/ClientRegistration")
    Call<UserModel> ClientRegister(@Part MultipartBody.Part imageFile,
                                   @Part("user_name") RequestBody user_name,
                                   @Part("user_pass")RequestBody user_pass,
                                   @Part("user_phone")RequestBody user_phone,
                                   @Part("user_full_name")RequestBody user_full_name,
                                   @Part("user_token_id")RequestBody user_token_id,
                                   @Part("user_google_lat")RequestBody user_google_lat,
                                   @Part("user_google_long")RequestBody user_google_long,
                                   @Part("user_email")RequestBody user_email,
                                   @Part("user_photo_send")RequestBody user_photo_send);
    @Multipart
    @POST("Api/DriverRegistration")
    Call<UserModel> DriverRegister(
                                   @Part("user_name") RequestBody user_name,
                                   @Part("user_pass")RequestBody user_pass,
                                   @Part("user_phone")RequestBody user_phone,
                                   @Part("user_full_name")RequestBody user_full_name,
                                   @Part("user_token_id")RequestBody user_token_id,
                                   @Part("user_google_lat")RequestBody user_google_lat,
                                   @Part("user_google_long")RequestBody user_google_long,
                                   @Part("user_email")RequestBody user_email,
                                   @Part("user_neighborhood")RequestBody user_neighborhood,
                                   @Part("user_city")RequestBody user_city,
                                   @Part MultipartBody.Part user_photo,
                                   @Part MultipartBody.Part  user_license_photo,
                                   @Part MultipartBody.Part  user_car_photo,
                                   @Part MultipartBody.Part  user_residence_photo
                                   );
    @FormUrlEncoded
    @POST("Api/Login")
    Call<UserModel> Login(@Field("user_name") String user_name,
                          @Field("user_pass") String user_pass);

    @GET("Api/Logout/{user_id}")
    Call<ResponseModel> logout(@Path("user_id")String user_id);

    @FormUrlEncoded
    @POST("Api/UpdateLocation/{user_id}")
    Call<ResponseModel> updateLocation(@Path("user_id") String user_id ,
                                       @Field("user_google_lat") String user_google_lat,
                                       @Field("user_google_long") String user_google_long
                                       );
    @Multipart
    @POST("Api/UpdateClientRegistration/{user_id}")
    Call<UserModel> UpdateClientImage(@Path("user_id") String user_id,
                                      @Part("user_name")RequestBody user_name,
                                      @Part("user_phone")RequestBody user_phone,
                                      @Part("user_full_name")RequestBody user_full_name,
                                      @Part("user_email")RequestBody user_email,
                                      @Part MultipartBody.Part user_photo);

    @Multipart
    @POST("Api/UpdateClientRegistration/{user_id}")
    Call<UserModel> UpdateClientData(@Path("user_id") String user_id,
                                     @Part("user_name") RequestBody user_name,
                                     @Part("user_phone")RequestBody user_phone,
                                     @Part("user_full_name")RequestBody user_full_name,
                                     @Part("user_email")RequestBody user_email);

    @Multipart
    @POST("Api/UpdateDriverRegistration/{user_id}")
    Call<UserModel> UpdateDriverImage(@Path("user_id") String user_id,
                                      @Part("user_name")RequestBody user_name,
                                      @Part("user_phone")RequestBody user_phone,
                                      @Part("user_full_name")RequestBody user_full_name,
                                      @Part("user_email")RequestBody user_email,
                                      @Part("user_neighborhood")RequestBody user_neighborhood,
                                      @Part("user_city")RequestBody user_city,
                                      @Part MultipartBody.Part photo);

    @Multipart
    @POST("Api/UpdateDriverRegistration/{user_id}")
    Call<UserModel> UpdateDriverData(@Path("user_id") String user_id,
                                      @Part("user_name") RequestBody user_name,
                                      @Part("user_phone")RequestBody user_phone,
                                      @Part("user_full_name")RequestBody user_full_name,
                                      @Part("user_email")RequestBody user_email,
                                      @Part("user_neighborhood")RequestBody user_neighborhood,
                                      @Part("user_city")RequestBody user_city);

    @FormUrlEncoded
    @POST("Api/UpdatePass/{user_id}")
    Call<UserModel> updatePassword(@Path("user_id") String user_id,@Field("user_old_pass")String user_old_pass,@Field("user_new_pass")String user_new_pass);

    @GET("Api/AllDepartProduct")
    Call<List<AllGroceries_SubCategory>> getAllGrocery_subcategories();

    @Multipart
    @POST("Api/GroceryRegistration")
    Call<UserModel> groceryRegister(@Part("user_name") RequestBody user_name,
                                    @Part("user_pass")RequestBody user_pass,
                                    @Part("user_phone")RequestBody user_phone,
                                    @Part("user_full_name")RequestBody user_full_name,
                                    @Part("user_token_id")RequestBody user_token_id,
                                    @Part("user_google_lat")RequestBody user_google_lat,
                                    @Part("user_google_long")RequestBody user_google_long,
                                    @Part("user_email")RequestBody user_email,
                                    @Part("user_work_hours")RequestBody user_work_hours,
                                    @Part("my_products[]")List<RequestBody> id_product,
                                    @Part MultipartBody.Part user_photo);
    @GET("Api/AppDetails/3")
    Call<RuleModel> getRuleData();


    @GET("Api/ContactUs")
    Call<ContactsModel> getContacts();

    @FormUrlEncoded
    @POST("Api/ContactUs")
    Call<ResponseModel> ContactUs(@FieldMap Map<String,String> map);

    @GET("Api/SuperMarket")
    Call<List<SuperMarketModel>> getSuperMarketCategories();

    @FormUrlEncoded
    @POST("Api/MinMarket")
    Call<List<MiniMarketDataModel>> getMiniMarketData(@Field("my_google_lat") double my_google_lat,@Field("my_google_long") double my_google_long);

    @POST("Api/AddMinOrder")
    Call<ResponseModel> sendOrderToMiniMarket(@Body List<ItemsModel> itemsModelList);

    @POST("Api/AddSuperOrder")
    Call<ResponseModel> sendOrderToSuperMarket(@Body List<ItemsModel> itemsModelList);

    @FormUrlEncoded
    @POST("Api/RestMyPass")
    Call<ResponseModel> resetPassword(@Field("user_name") String user_name,@Field("user_email") String user_email);

    @GET("Api/ClientAlerts/{user_id}")
    Call<List<Client_Notification_Model>> getClientNotifications(@Path("user_id") String user_id);

    @GET("Api/MyDeliveryOrders/{user_id}")
    Call<List<Driver_Grocery_Notification_Model>> getDriver_Notifications(@Path("user_id") String user_id);

    @GET("Api/GroceryAlerts/{user_id}")
    Call<List<Driver_Grocery_Notification_Model>> getGrocery_Notifications(@Path("user_id") String user_id);

    @GET("Api/ClintUnread/{user_id}")
    Call<UnreadModel> getClientUnreadNotification(@Path("user_id") String user_id);

    @FormUrlEncoded
    @POST("Api/ClintUnread/{user_id}")
    Call<UnreadModel> setClientReadNotification(@Path("user_id") String user_id, @Field("read_all") String read_all);


    @GET("Api/DeliverUnread/{user_id}")
    Call<UnreadModel> getDriver_Grocery_UnreadNotification(@Path("user_id") String user_id);

    @FormUrlEncoded
    @POST("Api/DeliverUnread/{user_id}")
    Call<UnreadModel> setDriver_Grocery_ReadNotification(@Path("user_id") String user_id, @Field("read_all") String read_all);

    @FormUrlEncoded
    @POST("Api/SearchProduct")
    Call<List<ItemsModel>> search(@Field("searh_title") String query);

    @FormUrlEncoded
    @POST("Api/ClientReply/{user_id}/{id_delivery_order}")
    Call<ResponseModel> clientReplyOrder(@Path("user_id")String user_id,
                                         @Path("id_delivery_order")String id_delivery_order,
                                         @Field("action") String action,
                                         @Field("bill_num_fk") String bill_num_fk,
                                         @Field("id_delivery_user_fk") String id_delivery_user_fk,
                                         @Field("market_type") String market_type);

    @FormUrlEncoded
    @POST("Api/DriverReply/{user_id}/{id_delivery_order}")
    Call<ResponseModel> driverReplyOrder(@Path("user_id")String user_id,
                                         @Path("id_delivery_order")String id_delivery_order,
                                         @Field("action") String action,
                                         @Field("bill_num_fk") String bill_num_fk,
                                         @Field("id_client_fk") String Id_client_fk);

    @FormUrlEncoded
    @POST("Api/GroceryReply/{user_id}/{id_delivery_order}")
    Call<ResponseModel> groceryReplyOrder(@Path("user_id")String user_id,
                                         @Path("id_delivery_order")String id_delivery_order,
                                         @Field("action") String action,
                                         @Field("bill_num_fk") String bill_num_fk,
                                         @Field("id_client_fk") String Id_client_fk);

    @GET("Api/ClientOrders/1/{user_id}")
    Call<List<ClientOrderModel>> getClientCurrentOrder(@Path("user_id") String user_id);

    @GET("Api/ClientOrders/2/{user_id}")
    Call<List<ClientOrderModel>> getClientPreviousOrder(@Path("user_id") String user_id);

    @GET("Api/MyOrders/1/{user_id}")
    Call<List<Driver_Grocery_OrderModel>> getDriver_Grocery_CurrentOrder(@Path("user_id") String user_id);

    @GET("Api/MyOrders/2/{user_id}")
    Call<List<Driver_Grocery_OrderModel>> getDriver_Grocery_PreviousOrder(@Path("user_id") String user_id);

    @GET("Api/CitesAreas")
    Call<List<CountryModel>> getCountries();

    @FormUrlEncoded
    @POST("Api/BillEnd/{bill_id}")
    Call<ResponseModel> endOrder(@Path("bill_id") String bill_id,
                                 @Field("user_type") String user_type,
                                 @Field("user_id") String user_id,
                                 @Field("id_delivery_user_fk") String id_delivery_user_fk,
                                 @Field("room_id") String room_id
                                 );




    @GET("Chating/ChatRoom/{room_id}")
    Call<List<MessageModel>> getAllMessage(@Path("room_id") String room_id);

    @FormUrlEncoded
    @POST("Chating/SendMessage/{room_id}")
    Call<MessageModel> sendMessage_text(@Path("room_id") String room_id,@FieldMap Map<String,String> map);

    @Multipart
    @POST("Chating/SendMessage/{room_id}")
    Call<MessageModel> sendMessage_image(@Path("room_id") String room_id,
                                         @Part("from_id")RequestBody from_id,
                                         @Part("to_id")RequestBody to_id,
                                         @Part("message")RequestBody message,
                                         @Part("message_type")RequestBody message_type,
                                         @Part MultipartBody.Part image_part
    );

    @FormUrlEncoded
    @POST("Chating/Typing/{room_id}")
    Call<ResponseBody> typing(@Path("room_id") String room_id, @FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("Api/UpdateTokenId/{user_id}")
    Call<ResponseModel> UpdateToken(@Path("user_id") String user_id,@Field("user_token_id")String token);
}
