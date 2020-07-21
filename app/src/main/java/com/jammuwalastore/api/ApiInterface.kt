package com.students.students

import com.google.gson.JsonObject
import com.jammuwalastore.model.ProductsData
import com.jammuwalastore.model.ResponseMessage
import com.jammuwalastore.model.SliderUrl
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by ABC on 19-Mar-18.
 */
interface ApiInterface {

    @Headers("Content-type: application/json", "Accept: */*")
    @POST("sendOtp")
    fun SendOtp(
        @Body jsonObject: JsonObject?
    ): Call<ResponseMessage>?


    @Headers("Content-type: application/json", "Accept: */*")
    @POST("verifyOtp")
    fun verifyOtp(
        @Body jsonObject: JsonObject?
    ): Call<ResponseMessage>?


    @Headers("Content-type: application/json", "Accept: */*")
    @POST("signup")
    fun signup(
        @Body jsonObject: JsonObject?
    ): Call<ResponseMessage>?


    @Headers("Content-type: application/json", "Accept: */*")
    @POST("signin")
    fun SignIn(
        @Body jsonObject: JsonObject?
    ): Call<ResponseBody>?

    @Headers("Content-type: application/json", "Accept: */*")
    @POST("sendresetpassword")
    fun SendResetPassword(
        @Body jsonObject: JsonObject?
    ): Call<ResponseMessage>?


    @Headers("Content-type: application/json", "Accept: */*")
    @POST("resetpassword")
    fun ResetPassword(
        @Body jsonObject: JsonObject?
    ): Call<ResponseMessage>?

    @Headers("Content-type: application/json", "Accept: */*")
    @GET("home/slider")
    fun slider(): Call<ArrayList<SliderUrl>>


    @Headers("Content-type: application/json", "Accept: */*")
    @GET("products")
    fun productsList(): Call<ArrayList<ProductsData>>



/*
    @GET("school_record.php")
    fun getBaseData() : Call<baseData>

    @POST("login")
    @FormUrlEncoded
    fun login(@Field("username") username : String,@Field("password") password : String) : Call<loginDetails>

    @GET("teacher_list")
    fun teachers() : Call<teacherDetails>

    @GET("parent_list")
    fun parents() : Call<parentsDetails>

    @GET("librarian_list")
    fun librarians() : Call<librariansDetails>

    @GET("accountant_list")
    fun accountants() : Call<accountantDetails>

    @GET("classes_list")
    fun classes() : Call<classesDetails>

    @GET("sections_list")
    fun sections() : Call<sectionsDetails>

    @GET("syllabus")
    fun syllabus() : Call<syllabusDetails>

    @GET("subject_list")
    fun subjects() : Call<subjectDetails>

    @GET("dashboard")
    fun dashboard() : Call<DashBoard>

    @GET("transport_list")
    fun transport() : Call<TransportList>

    @GET("dormitory_list")
    fun dormitory() : Call<DormitoryList>

    @GET("noticeboard_list")
    fun noticeboard() : Call<NoticeboardList>

    @POST("student_list")
    fun studentsList() : Call<StudentsList>

    @POST("exam_list")
    fun examsList() : Call<ExamsList>

    @POST("grade_list")
    fun gradesList() : Call<GradesList>

    @POST("library_list")
    fun booksList() : Call<BooksList>

    @POST("book_issue_list")
    fun bookIssueList() : Call<BookIssueList>

    @GET("expense_list")
    fun expensesList() : Call<ExpensesList>

    @GET("expense_category_list")
    fun expensesCategoryList() : Call<ExpensesCategoryList>

    @GET("student_payment_list")
    fun studentPaymentsList() : Call<StudentPaymentsList>

    @POST("bus_current_location")
    fun busLocations() : Call<BusLocations>

    @POST("daily_attendance")
    @FormUrlEncoded
    fun dailyAttendance(@Field("class") className : String,@Field("section") section : String,@Field("date") date:String,@Field("session") session:String) : Call<DailyAttendance>

    @POST("sections_list")
    @FormUrlEncoded
    fun sectionsBasedOnClass(@Field("class") className : String) : Call<sectionsDetails>

    @POST("daily_attendance_submit")
    @FormUrlEncoded
    fun dailyAttendanceSubmit(@Field("class") className : String,@Field("section") section : String,@Field("date")date:String,@Field("status")status:String,@Field("s_id")sid:String,@Field("name")name:String,@Field("mobile")mobile:String) : Call<JsonObject>

    @POST("attendance_report")
    @FormUrlEncoded
    fun attendanceReport(@Field("class") className : String,@Field("section")section:String,@Field("month")month:String,@Field("year")year:String) : Call<AttendanceReport>*/

}