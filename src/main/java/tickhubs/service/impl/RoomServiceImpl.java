package tickhubs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tickhubs.dto.ApiResponse;
import tickhubs.dto.RoomRequest;
import tickhubs.dto.RoomResponse;
import tickhubs.model.Room;
import tickhubs.model.User;
import tickhubs.repository.*;
import tickhubs.security.UserPrincipal;
import tickhubs.util.CoreUtil;
import tickhubs.util.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoomServiceImpl {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private RoomRepository roomRepository;

    public ApiResponse createRoom(RoomRequest roomRequest) {
        ApiResponse response = new ApiResponse(true, "Room Created Successfully");

        if (Objects.isNull(roomRequest.getType())) {
            response.setSuccess(false);
            response.setMessage("Please Provide RoomType");
            return response;
        }

        User loggedInUser = userRepository.getOne(CoreUtil.getCurrentUserId());

        Room room = Room.builder()
                .name(roomRequest.getName())
                .description(roomRequest.getDescription())
                .website(roomRequest.getWebsite())
                .createdByUser(loggedInUser)
                .roomType(roomRequest.getType())
                .build();
        roomRepository.save(room);
        return response;
    }

    public ApiResponse<List<RoomResponse>> getRoomByUsernameOrRoomName(String key) {

    	ApiResponse<List<RoomResponse>> response = new ApiResponse(true, "Fetch Room by username or roomname, Successfully");
    	Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        if (Objects.isNull(key)) {
            response.setSuccess(false);
            response.setMessage("Please Provide the Room key !");
            return response;
        }
        
		Page<Room> rooms = roomRepository.findAllByName(key, pageable);
		if (rooms.getContent().size() > 0) {
			return getRoomsResponse(response, rooms);
		} else {
			Page<Room> roomsByUserName = roomRepository.findAllByCreatedByUser_Username(key, pageable);
			return getRoomsResponse(response, roomsByUserName);
		}
    }

    public ApiResponse<List<RoomResponse>> getRoomByLoggedInUser() {
        ApiResponse<List<RoomResponse>> response = new ApiResponse(true, "Fetch Room by logged-in user Successfully");
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        Optional<User> userOptional = userRepository.findById(CoreUtil.getCurrentUserId());
        if(userOptional.isPresent()) {
            Page<Room> rooms = roomRepository.findAllByCreatedByUser(userOptional.get(), pageable);
            return getRoomsResponse(response, rooms);
        }
        return response;
    }

    private ApiResponse<List<RoomResponse>> getRoomsResponse(ApiResponse<List<RoomResponse>> response, Page<Room> rooms) {
        if(rooms.getNumberOfElements()!= 0){
            List<RoomResponse> roomResponses = rooms.map(room -> ModelMapper.mapRoomToRoomResponse(room, room.getCreatedByUser())).getContent();
            response.setData(roomResponses);
            return response;
        }
        return response;
    }

	public ApiResponse<List<RoomResponse>> deleteRoomByUser(String roomName) {
		ApiResponse<List<RoomResponse>> response = new ApiResponse(false, "Deleting Room by roomName. ");
		Optional<User> userOptional = userRepository.findById(CoreUtil.getCurrentUserId());
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
		if (userOptional.isPresent()) {
			// Page<Room> rooms =
			// roomRepository.deleteByNameAndCreatedByUser(roomName,
			// userOptional.get(), pageable);
			Page<Room> rooms = roomRepository.findAllByName(roomName, pageable);
			if (rooms.getContent().size() > 0) {
				roomRepository.deleteById(rooms.getContent().get(0).getId());
				ApiResponse<List<RoomResponse>> response2 = new ApiResponse(true, "Deleting Room by roomName, Succecssfully ! ");
				return response2;
			}
		}
		return response;
	}
}
