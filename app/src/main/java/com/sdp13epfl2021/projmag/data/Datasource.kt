package com.sdp13epfl2021.projmag.data

import com.sdp13epfl2021.projmag.model.Project

class Datasource {
    fun loadProjects() : List<Project> {
        return listOf<Project>(
                Project("What fraction of Google searches are answered by Wikipedia?","DLAB","Robert West","TA1",1, listOf<String>(),false,true, listOf("data analysis","large datasets"),false,"Description of project1"),
                Project("Real-time reconstruction of deformable objects","CVLAB","Teacher2","TA2",1, listOf<String>(),false,true, listOf("Computer Vision","ML"),false,"Description of project2"),
                Project("GUI Fuzzing","HexHive","Teacher3","TA3",2, listOf<String>(),true,false, listOf("Android","Fuzzing","RE"),false,"Description of project3"),
                Project("Study of tracker behavioral patterns","SPRING","Teacher1","TA1",1, listOf<String>(),false,true, listOf("Python","Web Dev"),false,"Description of project4"),
                Project("Implement a fast driver for a 100 Gb/s network card","DSLAB","Teacher5","TA5",3, listOf<String>(),false,true, listOf("Low Level","Networking","Driver"),false,"Description of project5")
        )
    }
}