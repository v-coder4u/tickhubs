package tickhubs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tickhubs.dto.ApiResponse;
import tickhubs.dto.RoomRequest;
import tickhubs.dto.RoomResponse;
import tickhubs.model.Room;
import tickhubs.model.User;
import tickhubs.repository.*;
import tickhubs.security.UserPrincipal;
import tickhubs.util.CoreUtil;

import java.util.Objects;

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

    public RoomResponse getPollByUsernameOrRoomName(Long name, UserPrincipal currentUser) {
        // TODO Auto-generated method stub
        return null;
    }

}
