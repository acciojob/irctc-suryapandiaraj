package com.driver.services;

import com.driver.EntryDto.AddTrainEntryDto;
import com.driver.EntryDto.SeatAvailabilityEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Station;
import com.driver.model.Ticket;
import com.driver.model.Train;
import com.driver.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainService {

    @Autowired
    TrainRepository trainRepository;

    public Integer addTrain(AddTrainEntryDto trainEntryDto){

        //Add the train to the trainRepository
        //and route String logic to be taken from the Problem statement.
        //Save the train and return the trainId that is generated from the database.
        //Avoid using the lombok library
        //String route, List<Ticket> bookedTickets, LocalTime departureTime, int noOfSeats
        Train train=new Train();
        train.setBookedTickets(new ArrayList<Ticket>());
        train.setRoute(String.valueOf(trainEntryDto.getStationRoute().get(0)));
        train.setDepartureTime(trainEntryDto.getDepartureTime());
        train.setNoOfSeats(trainEntryDto.getNoOfSeats());
        trainRepository.save(train);

        return train.getTrainId();
    }

    public Integer calculateAvailableSeats(SeatAvailabilityEntryDto seatAvailabilityEntryDto){

        //Calculate the total seats available
        //Suppose the route is A B C D
        //And there are 2 seats avaialble in total in the train
        //and 2 tickets are booked from A to C and B to D.
        //The seat is available only between A to C and A to B. If a seat is empty between 2 station it will be counted to our final ans
        //even if that seat is booked post the destStation or before the boardingStation
        //Inshort : a train has totalNo of seats and there are tickets from and to different locations
        //We need to find out the available seats between the given 2 stations.

        return null;
    }

    public Integer calculatePeopleBoardingAtAStation(Integer trainId,Station station) throws Exception{

        //We need to find out the number of people who will be boarding a train from a particular station
        //if the trainId is not passing through that station
        //throw new Exception("Train is not passing from this station");
        //  in a happy case we need to find out the number of such people.
        int totalPassengers=0;
        Train train = trainRepository.findById(trainId).get();
        if (!train.getRoute().contains(station.toString())){
            throw new Exception("Train is not passing from this station");
        }
        List<Ticket> ticketList = train.getBookedTickets();

        for(Ticket ticket : ticketList){
            if(ticket.getFromStation().equals(station.toString())){
                totalPassengers += ticket.getPassengersList().size();
            }
        }
        return totalPassengers;
    }

    public Integer calculateOldestPersonTravelling(Integer trainId){

        //Throughout the journey of the train between any 2 stations
        //We need to find out the age of the oldest person that is travelling the train
        //If there are no people travelling in that train you can return 0
        int oldest=Integer.MIN_VALUE;
        Train train = trainRepository.findById(trainId).get();
        List<Ticket> ticketList=train.getBookedTickets();
        for (Ticket ticket : ticketList){
            List<Passenger> passengerList = ticket.getPassengersList();
            for(Passenger passenger : passengerList){
                if(passenger.getAge() > oldest){
                    oldest = passenger.getAge();
                }
            }
        }
        return oldest == Integer.MIN_VALUE ? 0 : oldest;
    }

    public List<Integer> trainsBetweenAGivenTime(Station station, LocalTime startTime, LocalTime endTime){

        //When you are at a particular station you need to find out the number of trains that will pass through a given station
        //between a particular time frame both start time and end time included.
        //You can assume that the date change doesn't need to be done ie the travel will certainly happen with the same date (More details
        //in problem statement)
        //You can also assume the seconds and milli seconds value will be 0 in a LocalTime format.
        return null;
    }

}
