package org.rayhane.dzpharmz.Services;

import org.rayhane.dzpharmz.Model.Pharmacy;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Rayhane on 30/04/2017.
 */

public interface DzpharmsClient {

    @GET("pharmacies")
    Call<List<Pharmacy>> getPharms();

}
