package com.jammuwalastore.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.jammuwalastore.MainActivity
import com.jammuwalastore.R
import com.jammuwalastore.adapter.ProductListAdapter
import com.jammuwalastore.dataBase.DBHandler
import com.jammuwalastore.helper.Constants.Companion.Product_Details_url
import com.jammuwalastore.model.ProductModel
import com.jammuwalastore.model.ProductsData
import com.jammuwalastore.model.SliderUrl
import com.students.students.ApiInterface
import com.students.students.RetroBaseClass
import kotlinx.android.synthetic.main.home_fragment.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var root: View
    private var userList: ArrayList<SliderUrl>? = null
    private var productsList: ArrayList<ProductsData>? = ArrayList()
    private var productListAdapter: ProductListAdapter? = null
    var dbHandler: DBHandler? = null
    private var progressLayout: LinearLayout? = null;

    var totalItemCount = 0
    var lastVisibleItem = 0
    var visibleThreshold = 50
    var pageNumber = 0
    var loading = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.home_fragment, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressLayout = view.findViewById(R.id.progressLayout)
        dbHandler = DBHandler(view.context)
        setupUi()
    }



    private fun setupUi() {
        progressLayout?.visibility = View.VISIBLE
        slider()
        rvProductsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int, dy: Int
            )
            {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    rvProductsList.layoutManager as LinearLayoutManager?
                totalItemCount = linearLayoutManager!!.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!loading && totalItemCount <= lastVisibleItem + visibleThreshold) { //End of the items
                    pageNumber += 1
                    productsData()
                    loading = true
                }
            }
        })
        setupAdapter()
        pageNumber += 1
        productsData()
        setCartValue()
    }

    fun setupAdapter() {
        if (productsList != null) {
            productListAdapter = ProductListAdapter(activity, productsList,
                ProductListAdapter.onItemAdd { isPlus, position ->
                    if (isPlus)
                    {
                        var quantity: Int = 0
                        if (productsList?.get(position)?.quantity != null
                            && productsList?.get(position)?.quantity!! > 0
                        )
                        {
                            productsList?.get(position)?.quantity =
                                productsList?.get(position)?.quantity!! + 1
                            quantity = productsList?.get(position)?.quantity!!
                        }
                        else {
                            productsList?.get(position)?.quantity = 1
                            quantity = productsList?.get(position)?.quantity!!
                        }
                        val productAPIModel = productsList?.get(position)
                        val productModel = ProductModel()
                        productModel.productName = productAPIModel?.name
                        productModel.productid = productAPIModel?.id.toString()
                        productModel.productPrice = productAPIModel?.price
                        productModel.productActualPrice = productAPIModel?.regular_price
                        productModel.tax_status = productAPIModel?.tax_status
                        productModel.tax_class = productAPIModel?.tax_class
                        try {
                            if ((productAPIModel?.images?.get(0)?.src).isNullOrBlank()) {
                                productModel.productImage = ""
                            } else {
                                productModel.productImage = productAPIModel?.images?.get(0)?.src
                            }
                        } catch (ignored: Exception) {
                            productModel.productImage = ""
                        }
                        productModel.productQuantity = quantity
                        try {
                            if (productAPIModel?.attributes?.get(0)?.options?.get(0)?.length ?: 0 > 0) {
                                productModel.product_weight =
                                    productAPIModel?.attributes?.get(0)?.options?.get(0)
                                        .toString()
                            } else {
                                productModel.product_weight = ""
                            }
                        } catch (e: Exception) {
                            productModel.product_weight = ""
                        }
                        try {
                            var price: Long = 0;
                            price = productAPIModel?.price?.toLong() ?: 0
                            price *= quantity
                            productModel.totalPrice = price.toString()
                        } catch (e: Exception) {
                            productModel.totalPrice = productAPIModel?.price
                        }
                        if (quantity == 1) {
                            dbHandler?.addProduct(productModel)
                        } else {
                            dbHandler?.updateProducts(productModel)
                        }
                    } else {
                        var quantity: Int = 0
                        productsList?.get(position)?.quantity =
                            productsList?.get(position)?.quantity!! - 1

                        quantity = productsList?.get(position)?.quantity!!

                        val productAPIModel = productsList?.get(position)
                        if (quantity < 1) {
                            dbHandler?.deleteProduct(
                                productAPIModel?.id.toString(),
                                productAPIModel?.weight
                            )
                        } else {
                            val productModel: ProductModel = ProductModel()
                            productModel.productName = productAPIModel?.name
                            productModel.productid = productAPIModel?.id.toString()
                            productModel.productPrice = productAPIModel?.price
                            productModel.productActualPrice = productAPIModel?.regular_price
                            productModel.tax_status = productAPIModel?.tax_status
                            productModel.tax_class = productAPIModel?.tax_class


                            try {
                                if ((productAPIModel?.images?.get(0)?.src).isNullOrBlank()) {
                                    productModel.productImage = ""
                                } else {
                                    productModel.productImage = productAPIModel?.images?.get(0)?.src
                                }
                            } catch (ignored: Exception) {
                                productModel.productImage = ""
                            }
                            productModel.productQuantity = quantity
                            try {
                                if (productAPIModel?.attributes?.get(0)?.options?.get(0)?.length ?: 0 > 0) {
                                    productModel.product_weight =
                                        productAPIModel?.attributes?.get(0)?.options?.get(0)
                                            .toString()
                                } else {
                                    productModel.product_weight = ""
                                }
                            } catch (e: Exception) {
                                productModel.product_weight = ""
                            }
                            try {
                                var price: Long = 0;
                                price = productAPIModel?.price?.toLong() ?: 0
                                price *= quantity
                                productModel.totalPrice = price.toString()
                            } catch (e: Exception) {
                                productModel.totalPrice = productAPIModel?.price
                            }
                            dbHandler?.updateProducts(productModel)
                        }


                    }
                    setCartValue()
                    productListAdapter?.notifyDataSetChanged()
                })
            rvProductsList?.adapter = productListAdapter
        }

    }

    var slider = {
        val baseClass = RetroBaseClass()
        val apiInterface = baseClass.getRetroFit().create(ApiInterface::class.java)

        Log.e("slider ", "API -")
        val response: Call<ArrayList<SliderUrl>>? = apiInterface.slider()

        response?.enqueue(object : Callback<ArrayList<SliderUrl>> {

            override fun onResponse(
                call: Call<ArrayList<SliderUrl>>?,
                response: Response<ArrayList<SliderUrl>>?
            ) {

                Log.e("slider ", "API -" + call!!.request().url().toString())

                if (response!!.isSuccessful) {
                    userList = response.body()
                }

                Log.e("slider ", "response -" + (userList?.size ?: userList))

                val imageList = ArrayList<SlideModel>()
                for (SliderUrl in userList!!) {
                    imageList.add(
                        SlideModel(
                            SliderUrl.url,
                            false
                        )
                    )
                }

                //imageList.add(SlideModel("https://1.bp.blogspot.com/-GUZsgr8my50/XJUWOhyHyaI/AAAAAAAABUo/bljp3LCS3SUtj-judzlntiETt7G294WcgCLcBGAs/s1600/fox.jpg", "Foxes live wild in the city.", true))

                val imageSlider = root.findViewById<ImageSlider>(R.id.image_slider)
                imageSlider.setImageList(imageList, true)
                imageSlider.startSliding(7000)

            }

            override fun onFailure(call: Call<ArrayList<SliderUrl>>?, t: Throwable?) {
                //dialog.dismiss()

            }
        })

    }

    var productsData = {
        //  dialog.show()
        val baseClass = RetroBaseClass()
        val apiInterface = baseClass.getRetroFitHome().create(ApiInterface::class.java)

        Log.e("productsList ", "API -")
        val response: Call<ArrayList<ProductsData>>? = apiInterface.productsList()

        response?.enqueue(object : Callback<ArrayList<ProductsData>> {

            override fun onResponse(
                call: Call<ArrayList<ProductsData>>?,
                response: Response<ArrayList<ProductsData>>?
            ) {

                Log.e("slider ", "API -" + call!!.request().url().toString())

                //dialog.dismiss()
                if (response!!.isSuccessful) {
                    /*var data = response.body()!!.arrayList
                    if (data.size > 0) {

                    }*/
                    productsList = response.body()
                }

                Log.e("productsList ", "response -" + (productsList?.size ?: productsList))
                setupQuantity()

            }

            override fun onFailure(call: Call<ArrayList<ProductsData>>?, t: Throwable?) {
                progressLayout?.visibility = View.GONE
            }
        })

    }

    private fun productsData() {
        progressLayout?.setVisibility(View.VISIBLE)
        val url =
            "$Product_Details_url?stock_status=instock&per_page=$visibleThreshold&page=$pageNumber"
        Log.e("productsData url>>", "$url<<")
        AndroidNetworking.get(url)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    val productModelList: ArrayList<ProductsData> =
                        fromJsonList(
                            response.toString(),
                            ProductsData::class.java
                        )
                    if (productModelList.size > 0 && visibleThreshold == productModelList.size) {
                        loading = false
                    }
                    productsList?.addAll(productModelList)
                    setupQuantity()
                    Log.e(
                        "productsDataArrayList>>",
                        productsList?.size.toString() + "<<"
                    )
                    progressLayout?.setVisibility(View.GONE)
                }

                override fun onError(anError: ANError) {
                    Log.e("error", anError.toString() + "")
                    progressLayout?.setVisibility(View.GONE)
                }
            })
    }


    private fun fromJsonList(
        json: String,
        clazz: Class<ProductsData>
    ): ArrayList<ProductsData> {
        var array =
            java.lang.reflect.Array.newInstance(clazz, 0) as Array<Any?>
        array = Gson().fromJson(json, array.javaClass)
        val list: ArrayList<ProductsData> = java.util.ArrayList()
        for (o in array) clazz.cast(o)?.let { list.add(it) }
        return list
    }

    private fun setupQuantity() {
        if (dbHandler == null || productsList?.size ?: 0 < 1) {
            return
        }
        for (i in 0 until (productsList?.size ?: 0)) {
            productsList?.get(i)?.quantity = 0
        }

        val list = dbHandler?.allProducts
        if (list != null) {
            for (i in 0 until (productsList?.size ?: 0)) {
                for (productModel in list) {
                    if (productModel.productid.equals(productsList?.get(i)?.id.toString(), true)) {
                        productsList?.get(i)?.quantity = productModel.productQuantity
                    }
                }
            }

        }

        productListAdapter?.notifyDataSetChanged()

        progressLayout?.visibility = View.GONE
    }

    private fun setCartValue() {
        if (dbHandler == null) return
        val list = dbHandler?.allProducts
        val parentActivity = activity as MainActivity
        if (list != null && list.size > 0) {
            parentActivity.txtCartItemsCount.visibility = View.VISIBLE
            parentActivity.txtCartItemsCount.text = list.size.toString()
        } else {
            parentActivity.txtCartItemsCount.visibility = View.GONE
        }

    }

    override fun onResume() {
        super.onResume()
        try {
            setupQuantity()
            setCartValue()
            val parentActivity = activity as MainActivity
            parentActivity.txtTitle.text = getString(R.string.menu_home);
        } catch (e: Exception) {
        }
    }

}
