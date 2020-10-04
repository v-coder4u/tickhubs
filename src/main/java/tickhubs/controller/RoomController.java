package tickhubs.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tickhubs.dto.ApiResponse;
import tickhubs.dto.PollRequest;
import tickhubs.dto.RoomRequest;
import tickhubs.dto.RoomResponse;
import tickhubs.model.Poll;
import tickhubs.model.Room;
import tickhubs.repository.PollRepository;
import tickhubs.repository.RoomRepository;
import tickhubs.repository.UserRepository;
import tickhubs.repository.VoteRepository;
import tickhubs.service.impl.PollServiceImpl;
import tickhubs.service.impl.RoomServiceImpl;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
	@Autowired
	private RoomServiceImpl roomServiceImpl;

	private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ApiResponse createRoom(@RequestBody RoomRequest roomRequest) {
		return roomServiceImpl.createRoom(roomRequest);
	}

	@GetMapping("/fetchRoom/{key}")
	@PreAuthorize("hasRole('USER')")
	public ApiResponse<List<RoomResponse>> fetchRoomByUsernameOrRoomname(@PathVariable String key){
		return roomServiceImpl.getRoomByUsernameOrRoomName(key);
	}

	@GetMapping("/fetchRoomByUser")
	@PreAuthorize("hasRole('USER')")
	public ApiResponse<List<RoomResponse>> fetchRoomByLoggedInUser(){
		return roomServiceImpl.getRoomByLoggedInUser();
	}

	@GetMapping("/deleteRoomByUser/{roomName}")
	@PreAuthorize("hasRole('USER')")
	public ApiResponse<List<RoomResponse>> deleteRoomByUser(@PathVariable String roomName){
		return roomServiceImpl.deleteRoomByUser(roomName);
	}
}
