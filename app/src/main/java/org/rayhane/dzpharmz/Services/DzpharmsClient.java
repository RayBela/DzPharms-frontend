package org.rayhane.dzpharmz.Services;

import com.google.android.gms.maps.model.LatLng;

import org.rayhane.dzpharmz.Model.Pharmacy;
import org.rayhane.dzpharmz.Model.PostBody;
import org.rayhane.dzpharmz.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Rayhane on 30/04/2017.
 */

public interface DzpharmsClient {

    @GET("pharmacies")
    Call<List<Pharmacy>> getPharms();

    @Headers("Content-Type:application/json")
    @POST("nearPharmacies")
    Call<List<Pharmacy>> getNearestPharms(@Body PostBody location);

}
