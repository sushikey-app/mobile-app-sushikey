package com.am.projectinternalresto.service.api

import com.am.projectinternalresto.data.body_params.AdminAndSuperAdminRequest
import com.am.projectinternalresto.data.body_params.CategoryRequest
import com.am.projectinternalresto.data.body_params.LocationRequest
import com.am.projectinternalresto.data.body_params.LoginRequest
import com.am.projectinternalresto.data.body_params.OrderRequest
import com.am.projectinternalresto.data.body_params.PaymentRequest
import com.am.projectinternalresto.data.body_params.SaveOrderRequest
import com.am.projectinternalresto.data.body_params.StaffRequest
import com.am.projectinternalresto.data.response.GeneralResponse
import com.am.projectinternalresto.data.response.admin.category.AddOrUpdateCategoryResponse
import com.am.projectinternalresto.data.response.admin.category.CategoryResponse
import com.am.projectinternalresto.data.response.admin.manage_staff.AddOrUpdateStaffResponse
import com.am.projectinternalresto.data.response.admin.manage_staff.StaffResponse
import com.am.projectinternalresto.data.response.admin.menu.AddOrUpdateMenuResponse
import com.am.projectinternalresto.data.response.admin.menu.MenuResponse
import com.am.projectinternalresto.data.response.auth.LoginResponse
import com.am.projectinternalresto.data.response.staff.order.CancelResponse
import com.am.projectinternalresto.data.response.staff.order.ListOrderResponse
import com.am.projectinternalresto.data.response.staff.order.OrderResponse
import com.am.projectinternalresto.data.response.staff.order.PayResponse
import com.am.projectinternalresto.data.response.staff.riwayat_order.HistoryOrderResponse
import com.am.projectinternalresto.data.response.super_admin.cancel_order.ListCancelResponse
import com.am.projectinternalresto.data.response.super_admin.dashboard.MenuFavoriteResponse
import com.am.projectinternalresto.data.response.super_admin.dashboard.SalesDataResponse
import com.am.projectinternalresto.data.response.super_admin.location.AddOrUpdateLocationResponse
import com.am.projectinternalresto.data.response.super_admin.location.LocationResponse
import com.am.projectinternalresto.data.response.super_admin.manage_admin.AddOrUpdateAdminSuperAdminResponse
import com.am.projectinternalresto.data.response.super_admin.manage_admin.ManageAdminAndSuperAdminResponse
import com.am.projectinternalresto.data.response.super_admin.report.DetailReportResponse
import com.am.projectinternalresto.data.response.super_admin.report.ListReportResponse
import com.am.projectinternalresto.data.response.super_admin.report.PrintReportResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    /*user auth*/
    //login
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    /*Super Admin*/
    // dashboard
    @GET("dashboard-super-admin")
    suspend fun getDataSales(
        @Header("Authorization") bearer: String
    ): Response<SalesDataResponse>


    // filter dashboard
    @GET("filter-menu-favorit-super-admin")
    suspend fun getMenuFavoriteSuperAdmin(
        @Header("Authorization") bearer: String,
        @Query("lokasi_id") locationId: String
    ): Response<MenuFavoriteResponse>

    // location
    @GET("lokasi")
    suspend fun getLocation(
        @Header("Authorization") bearer: String
    ): Response<LocationResponse>

    @GET("lokasi")
    suspend fun searchLocation(
        @Header("Authorization") bearer: String,
        @Query("keyword") keyword: String
    ): Response<LocationResponse>

    @POST("lokasi")
    suspend fun addLocation(
        @Header("Authorization") bearer: String,
        @Body locationRequest: LocationRequest
    ): Response<AddOrUpdateLocationResponse>

    @PUT("lokasi/{id}")
    suspend fun updateLocation(
        @Header("Authorization") bearer: String,
        @Path("id") idLocation: String,
        @Body locationRequest: LocationRequest
    ): Response<AddOrUpdateLocationResponse>

    @DELETE("lokasi/{id}")
    suspend fun deleteLocation(
        @Header("Authorization") bearer: String,
        @Path("id") idLocation: String,
    ): Response<GeneralResponse>

    // manage admin and super admin
    @GET("pegawai-super-admin")
    suspend fun getAllDataAdminAndSuperAdmin(
        @Header("Authorization") bearer: String
    ): Response<ManageAdminAndSuperAdminResponse>

    @GET("pegawai-super-admin")
    suspend fun searchAdminAndSuperAdmin(
        @Header("Authorization") bearer: String,
        @Query("keyword") keyword: String
    ): Response<ManageAdminAndSuperAdminResponse>

    @POST("pegawai-super-admin")
    suspend fun addAdminOrSuperAdmin(
        @Header("Authorization") bearer: String,
        @Body addAdminAndSuperAdmin: AdminAndSuperAdminRequest
    ): Response<AddOrUpdateAdminSuperAdminResponse>

    @PUT("pegawai-super-admin/{id}")
    suspend fun updateAdminOrSuperAdmin(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
        @Body updateAdminOrSuperAdmin: AdminAndSuperAdminRequest
    ): Response<AddOrUpdateAdminSuperAdminResponse>

    @DELETE("pegawai-super-admin/{id}")
    suspend fun deleteAdminOrSuperAdmin(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
    ): Response<GeneralResponse>

    @GET("super-admin/laporan")
    suspend fun getListDataReportSuperAdmin(
        @Header("Authorization") bearer: String,
    ): Response<ListReportResponse>

    @GET("super-admin/laporan/{id}")
    suspend fun getDetailReportSuperAdmin(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
    ): Response<DetailReportResponse>

    @GET("super-admin/laporan/filter-print-data")
    suspend fun getReportForPrint(
        @Header("Authorization") bearer: String,
        @Query("lokasi_id") locationId: String,
        @Query("filter_tanggal_awal") startDate: Int,
        @Query("filter_bulan_awal") startMonth: Int,
        @Query("filter_tahun_awal") startYear: Int,
        @Query("filter_tanggal_akhir") endDate: Int,
        @Query("filter_bulan_akhir") endMonth: Int,
        @Query("filter_tahun_akhir") endYear: Int,
    ): Response<PrintReportResponse>

    @GET("super-admin/laporan/filter-laporan")
    suspend fun filterReportSuperAdmin(
        @Header("Authorization") bearer: String,
        @Query("lokasi_id") locationId: String,
        @Query("filter_tanggal_awal") startDate: Int,
        @Query("filter_bulan_awal") startMonth: Int,
        @Query("filter_tahun_awal") startYear: Int,
        @Query("filter_tanggal_akhir") endDate: Int,
        @Query("filter_bulan_akhir") endMonth: Int,
        @Query("filter_tahun_akhir") endYear: Int,
    ): Response<ListReportResponse>

    @DELETE("super-admin/laporan/filter-delete-data")
    suspend fun deleteReport(
        @Header("Authorization") bearer: String,
        @Query("lokasi_id") locationId: String,
        @Query("filter_tanggal_awal") startDate: Int,
        @Query("filter_bulan_awal") startMonth: Int,
        @Query("filter_tahun_awal") startYear: Int,
        @Query("filter_tanggal_akhir") endDate: Int,
        @Query("filter_bulan_akhir") endMonth: Int,
        @Query("filter_tahun_akhir") endYear: Int,
    ): Response<GeneralResponse>

    @GET("admin/laporan")
    suspend fun getListDataReportAdmin(
        @Header("Authorization") bearer: String,
    ): Response<ListReportResponse>

    @GET("admin/laporan/{id}")
    suspend fun getDetailReportAdmin(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
    ): Response<DetailReportResponse>

    @GET("admin/laporan/filter-print-data")
    suspend fun getDataPrintForAdmin(
        @Header("Authorization") bearer: String,
        @Query("filter_tanggal_awal") startDate: Int,
        @Query("filter_bulan_awal") startMonth: Int,
        @Query("filter_tahun_awal") startYear: Int,
        @Query("filter_tanggal_akhir") endDate: Int,
        @Query("filter_bulan_akhir") endMonth: Int,
        @Query("filter_tahun_akhir") endYear: Int,
    ): Response<PrintReportResponse>

    @GET("admin/laporan/filter-laporan")
    suspend fun filterReportAdmin(
        @Header("Authorization") bearer: String,
        @Query("filter_tanggal_awal") startDate: Int,
        @Query("filter_bulan_awal") startMonth: Int,
        @Query("filter_tahun_awal") startYear: Int,
        @Query("filter_tanggal_akhir") endDate: Int,
        @Query("filter_bulan_akhir") endMonth: Int,
        @Query("filter_tahun_akhir") endYear: Int,
    ): Response<ListReportResponse>

    @GET("super-admin/laporan/filter-by-lokasi/{id}")
    suspend fun filterReportByLocation(
        @Header("Authorization") bearer: String,
        @Path("id") locationId: String,
    ): Response<ListReportResponse>

    // Admin
    @GET("filter-menu-favorit-admin")
    suspend fun getMenuFavoriteAdmin(
        @Header("Authorization") bearer: String,
        @Query("kategori_id") categoryId: String
    ): Response<MenuFavoriteResponse>

    @GET("dashboard-admin")
    suspend fun getDataSalesAdmin(
        @Header("Authorization") bearer: String
    ): Response<SalesDataResponse>

    // category
    @GET("kategori")
    suspend fun getCategoryMenu(
        @Header("Authorization") bearer: String,
    ): Response<CategoryResponse>

    @GET("kategori")
    suspend fun searchCategoryMenu(
        @Header("Authorization") bearer: String,
        @Query("keyword") keyword: String
    ): Response<CategoryResponse>

    @POST("kategori")
    suspend fun addCategoryMenu(
        @Header("Authorization") bearer: String,
        @Body categoryBody: CategoryRequest
    ): Response<AddOrUpdateCategoryResponse>

    @PUT("kategori/{id}")
    suspend fun updateCategoryMenu(
        @Header("Authorization") bearer: String,
        @Path("id") idCategory: String,
        @Body categoryRequest: CategoryRequest
    ): Response<AddOrUpdateCategoryResponse>

    @DELETE("kategori/{id}")
    suspend fun deleteCategoryMenu(
        @Header("Authorization") bearer: String,
        @Path("id") idCategory: String,
    ): Response<GeneralResponse>

    // menu
    @GET("menu")
    suspend fun getAllDataMenu(
        @Header("Authorization") bearer: String
    ): Response<MenuResponse>

    @GET("filter-menu-by-kategori/{id}")
    suspend fun filterMenuByCategory(
        @Header("Authorization") bearer: String,
        @Path("id") idCategory: String
    ): Response<MenuResponse>

    @GET("filter-menu-pesan-by-kategori/{id}")
    suspend fun filterMenuPesanByCategory(
        @Header("Authorization") bearer: String,
        @Path("id") idCategory: String
    ): Response<MenuResponse>

    @Multipart
    @POST("menu")
    suspend fun addMenu(
        @Header("Authorization") bearer: String,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part?
    ): Response<AddOrUpdateMenuResponse>

    @Multipart
    @POST("menu/{id}")
    suspend fun updateMenu(
        @Header("Authorization") token: String,
        @Path("id") idMenu: String,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part?
    ): Response<AddOrUpdateMenuResponse>

    @DELETE("menu/{id}")
    suspend fun deleteMenu(
        @Header("Authorization") bearer: String,
        @Path("id") idMenu: String,
    ): Response<GeneralResponse>

    @GET("menu")
    suspend fun searchMenu(
        @Header("Authorization") bearer: String,
        @Query("keyword") keyword: String
    ): Response<MenuResponse>


    @GET("pegawai-admin")
    suspend fun getAllDataStaff(@Header("Authorization") bearer: String): Response<StaffResponse>

    @GET("pegawai-admin")
    suspend fun searchStaff(
        @Header("Authorization") bearer: String,
        @Query("keyword") keyword: String
    ): Response<StaffResponse>

    @POST("pegawai-admin")
    suspend fun addStaff(
        @Header("Authorization") bearer: String,
        @Body staffRequest: StaffRequest
    ): Response<AddOrUpdateStaffResponse>

    @PUT("pegawai-admin/{id}")
    suspend fun updateStaff(
        @Header("Authorization") bearer: String,
        @Path("id") idStaff: String,
        @Body staffRequest: StaffRequest
    ): Response<AddOrUpdateStaffResponse>

    @DELETE("pegawai-admin/{id}")
    suspend fun deleteStaff(
        @Header("Authorization") bearer: String,
        @Path("id") idStaff: String,
    ): Response<GeneralResponse>


    // staff
    @GET("pesan")
    suspend fun getDataOrder(@Header("Authorization") bearer: String): Response<MenuResponse>

    @GET("pesan")
    suspend fun searchMenuOrder(
        @Header("Authorization") bearer: String,
        @Query("keyword") keyword: String
    ) : Response<MenuResponse>

    @POST("simpan-pesanan")
    suspend fun saveDataOrder(
        @Header("Authorization") bearer: String,
        @Body orderRequest: SaveOrderRequest
    ): Response<OrderResponse>

    // function untuk bayar orderan dari lanjutan order pesanan
    @POST("pesan")
    suspend fun payFromOrderContinuation(
        @Header("Authorization") bearer: String,
        @Body body: OrderRequest
    ): Response<PayResponse>

    @GET("pesanan")
    suspend fun getListOrder(
        @Header("Authorization") bearer: String,
        @Query("status") paidStatus: String
    ): Response<ListOrderResponse>

    @PUT("pesanan/{id}")
    suspend fun changeStatusOrder(
        @Header("Authorization") bearer: String,
        @Path("id") idOrder: String,
    ): Response<OrderResponse>

    @PUT("bayar/{id}")
    suspend fun paymentOrder(
        @Header("Authorization") bearer: String,
        @Path("id") idOrder: String,
        @Body body: PaymentRequest
    ): Response<PayResponse>

    @GET("riwayat-pesanan")
    suspend fun getHistoryOrder(@Header("Authorization") bearer: String): Response<HistoryOrderResponse>

    @GET("pesanan/{id}")
    suspend fun getDetailOrder(
        @Header("Authorization") bearer: String,
        @Path("id") idOrder: String
    ): Response<OrderResponse>

    @GET("riwayat-pesanan/{id}")
    suspend fun getDetailHistoryOrder(
        @Header("Authorization") bearer: String,
        @Path("id") idOrder: String
    ): Response<OrderResponse>

    @GET("pembatalan-pesanan")
    suspend fun getCancelOrder(
        @Header("Authorization") bearer: String,
    ): Response<ListCancelResponse>

    @PUT("pesanan/cancel/{id}")
    suspend fun cancelOrder(
        @Header("Authorization") bearer: String,
        @Path("id") idOrder: String,
    ): Response<CancelResponse>

    @FormUrlEncoded
    @PUT("pembatalan-pesanan/{id}")
    suspend fun confirmCancelOrder(
        @Header("Authorization") bearer: String,
        @Path("id") idOrder: String,
        @Field("status") status: String,
        @Field("alasan_pembatalan") reason: String,
    ): Response<CancelResponse>

}