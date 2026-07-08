package com.services;

import org.hibernate.SessionFactory;

public interface RoomService {

    void saveRoom(SessionFactory sf);
    void getAllRooms(SessionFactory sf);
    void getRoom(SessionFactory sf);
    void updateRoom(SessionFactory sf);
    void deleteRoom(SessionFactory sf);
}