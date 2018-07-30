package com.semicolon.criuse.Services;

import com.semicolon.criuse.Models.AllGroceries_SubCategory;
import com.semicolon.criuse.Models.ResponseModel;
import com.semicolon.criuse.Models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
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
    @FormUrlEncoded
    @POST("Api/UpdateClientRegistration/{user_id}")
    Call<UserModel> UpdateClientImage(@Path("user_id") String user_id,
                                      @Part("user_name")RequestBody user_name,
                                      @Part("user_phone")RequestBody user_phone,
                                      @Part("user_full_name")RequestBody user_full_name,
                                      @Part("user_email")RequestBody user_email,
                                      MultipartBody.Part user_photo);

    @FormUrlEncoded
    @POST("Api/UpdateClientRegistration/{user_id}")
    Call<UserModel> UpdateClientData(@Path("user_id") String user_id,
                                     @Part("user_name") RequestBody user_name,
                                     @Part("user_phone")RequestBody user_phone,
                                     @Part("user_full_name")RequestBody user_full_name,
                                     @Part("user_email")RequestBody user_email);

    @FormUrlEncoded
    @POST("Api/UpdateDriverRegistration/{user_id}")
    Call<UserModel> UpdateDriverImage(@Path("user_id") String user_id,
                                      @Part("user_name")RequestBody user_name,
                                      @Part("user_phone")RequestBody user_phone,
                                      @Part("user_full_name")RequestBody user_full_name,
                                      @Part("user_email")RequestBody user_email,
                                      @Part("user_neighborhood")RequestBody user_neighborhood,
                                      @Part("user_city")RequestBody user_city,
                                      MultipartBody.Part photo);

    @FormUrlEncoded
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

}
