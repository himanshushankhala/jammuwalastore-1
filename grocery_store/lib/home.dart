import 'package:flutter/material.dart';
import 'package:grocery_store/banner_slider.dart';
import 'package:grocery_store/categories.dart';
import 'package:grocery_store/color.dart';
import 'package:grocery_store/main_drawer.dart';

class Home extends StatefulWidget {
  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0.1,
        backgroundColor: mainColor,
        title: Text("Grocery App"),
        actions: <Widget>[
          IconButton(
            icon: Icon(
              Icons.search,
              color: iconColor,
            ),
            onPressed: () {},
          ),
          IconButton(
            icon: Icon(
              Icons.shopping_cart,
              color: iconColor,
            ),
            onPressed: () {},
          ),
        ],
      ),

      //drawer
      drawer: MainDrawer(),

      //body
      body: ListView(
        children: <Widget>[
          Column(
            children: <Widget>[
              BannerSlider(),
              Categories(),
            ],
          ),
        ],
      ),
    );
  }
}
