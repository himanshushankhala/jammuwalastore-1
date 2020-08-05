import 'package:flutter/material.dart';
import 'package:grocery_store/categories_wise.dart';

class Categories extends StatefulWidget {
  @override
  _CategoriesState createState() => _CategoriesState();
}

class _CategoriesState extends State<Categories> {
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Column(
      children: <Widget>[
        TabBar(
          indicatorColor: Colors.green,
          indicatorWeight: 3.0,
          labelColor: Colors.black,
          unselectedLabelColor: Colors.grey,
          isScrollable: true,
          tabs: <Widget>[
            Tab(
              child: Text(
                "Fruits",
                style: TextStyle(
                    fontSize: 20,
                    fontFamily: 'OpenSans',
                ),
              ),
            ),
            Tab(
              child: Text(
                "Vegetables",
                style: TextStyle(
                  fontSize: 20,
                  fontFamily: 'OpenSans',
                ),
              ),
            ),
            Tab(
              child: Text(
                "Nuts & Seeds",
                style: TextStyle(
                  fontSize: 20,
                  fontFamily: 'OpenSans',
                ),
              ),
            ),
            Tab(
              child: Text(
                "Dairy",
                style: TextStyle(
                  fontSize: 20,
                  fontFamily: 'OpenSans',
                ),
              ),
            ),
          ],
        ),
        Expanded(
          child: Container(
            child: TabBarView(
              //controller: tabController,
                children: <Widget>[
                  Fruits(),
                  Vegetables(),
                  Nuts(),
                  Dairy(),
                ]),
          ),
        ),
      ],
    );
  }
}
