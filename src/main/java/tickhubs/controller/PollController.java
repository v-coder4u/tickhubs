package tickhubs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tickhubs.model.Poll;
import tickhubs.dto.ApiResponse;
import tickhubs.dto.PagedResponse;
import tickhubs.dto.PollRequest;
import tickhubs.dto.PollResponse;
import tickhubs.dto.VoteRequest;
import tickhubs.repository.PollRepository;
import tickhubs.repository.UserRepository;
import tickhubs.repository.VoteRepository;
import tickhubs.security.CurrentUser;
import tickhubs.security.UserPrincipal;
import tickhubs.service.impl.PollServiceImpl;
import tickhubs.util.AppConstants;

import java.net.URI;

/**
 * $ created by Vaibhav Varshney on : Sep 05, 2020
 */

@RestController
@RequestMapping("/api/polls")
public class PollController {

	@Autowired
	private PollRepository pollRepository;

	@Autowired
	private VoteRepository voteRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PollServiceImpl pollServiceImpl;

	private static final Logger logger = LoggerFactory.getLogger(PollController.class);

	@GetMapping
	public PagedResponse<PollResponse> getPolls(@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return pollServiceImpl.getAllPolls(currentUser, page, size);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> createPoll(@RequestBody PollRequest pollRequest) {
		Poll poll = pollServiceImpl.createPoll(pollRequest);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{pollId}").buildAndExpand(poll.getId())
				.toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "Poll Created Successfully"));
	}

	@GetMapping("/{pollId}")
	public PollResponse getPollById(@CurrentUser UserPrincipal currentUser, @PathVariable Long pollId) {
		return pollServiceImpl.getPollById(pollId, currentUser);
	}

	@PostMapping("/{pollId}/votes")
	@PreAuthorize("hasRole('USER')")
	public PollResponse castVote(@CurrentUser UserPrincipal currentUser, @PathVariable Long pollId, @RequestBody VoteRequest voteRequest) {
		return pollServiceImpl.castVoteAndGetUpdatedPoll(pollId, voteRequest, currentUser);
	}

}
