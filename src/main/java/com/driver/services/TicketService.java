package com.driver.services;


import com.driver.EntryDto.BookTicketEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Ticket;
import com.driver.model.Train;
import com.driver.repository.PassengerRepository;
import com.driver.repository.TicketRepository;
import com.driver.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    PassengerRepository passengerRepository;


    public Integer bookTicket(BookTicketEntryDto bookTicketEntryDto)throws Exception {

        //Check for validity
        //Use bookedTickets List from the TrainRepository to get bookings done against that train
        // Incase the there are insufficient tickets
        // throw new Exception("Less tickets are available");
        //otherwise book the ticket, calculate the price and other details
        //Save the information in corresponding DB Tables
        //Fare System : Check problem statement
        //Incase the train doesn't pass through the requested stations
        //throw new Exception("Invalid stations");
        //Save the bookedTickets in the train Object
        //Also in the passenger Entity change the attribute bookedTickets by using the attribute bookingPersonId.
        //And the end return the ticketId that has come from db
        Train train = trainRepository.findById(bookTicketEntryDto.getTrainId()).get();
        Passenger passenger = passengerRepository.findById(bookTicketEntryDto.getBookingPersonId()).get();
        int vacancy = (train.getNoOfSeats() - train.getBookedTickets().size());
        if (vacancy < bookTicketEntryDto.getNoOfSeats()) {
            throw new Exception("Less tickets are available");
        }
        if (!train.getRoute().contains(bookTicketEntryDto.getFromStation().toString()) || !train.getRoute().contains(bookTicketEntryDto.getToStation().toString())) {
            throw new Exception("Invalid stations");
        }

        Ticket ticket = new Ticket();
        ticket.setFromStation(bookTicketEntryDto.getFromStation());
        ticket.setToStation(bookTicketEntryDto.getToStation());
        ticket.setTrain(train);
        ticket.setPassengersList(passengerRepository.findAllById(bookTicketEntryDto.getPassengerIds()));

        Passenger bookingPerson = passengerRepository.findById(bookTicketEntryDto.getBookingPersonId()).get();
        bookingPerson.getBookedTickets().add(ticket);

        int totalFare = 0;
        int from = train.getRoute().indexOf(bookTicketEntryDto.getFromStation().toString());
        int to = train.getRoute().indexOf(bookTicketEntryDto.getToStation().toString());
        totalFare = (from - to) * 300;
        ticket.setTotalFare(totalFare);

        ticketRepository.save(ticket);

        return ticket.getTicketId();
    }
}
