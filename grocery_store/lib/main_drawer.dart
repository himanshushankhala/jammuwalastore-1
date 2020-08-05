import 'package:flutter/material.dart';
import 'package:grocery_store/color.dart';

class MainDrawer extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: ListView(
        children: <Widget>[
          //header
          UserAccountsDrawerHeader(
            accountName: Text('Welcome'),
            accountEmail: Text('Guest User'),
            currentAccountPicture: GestureDetector(
              child: new CircleAvatar(
                backgroundColor: Colors.grey,
                child: Icon(
                  Icons.person,
                  color: iconColor,
                ),
              ),
            ),
            decoration: BoxDecoration(
              color: mainColor,
            ),
          ),

          //body
          InkWell(
            onTap: () {},
            child: ListTile(
              title: Text('Home'),
              leading: Icon(Icons.home),
            ),
          ),

          InkWell(
            onTap: () {},
            child: ListTile(
              title: Text('My account'),
              leading: Icon(Icons.person),
            ),
          ),

          InkWell(
            onTap: () {},
            child: ListTile(
              title: Text('My Order'),
              leading: Icon(Icons.home),
            ),
          ),

          InkWell(
            onTap: () {},
            child: ListTile(
              title: Text('Help'),
              leading: Icon(Icons.help, color: Colors.blue,),
            ),
          ),

        ],
      ),
    );
  }
}
