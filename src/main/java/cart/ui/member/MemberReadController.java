package cart.ui.member;

import cart.application.service.member.MemberReadService;
import cart.application.service.member.dto.MemberResultDto;
import cart.ui.member.dto.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/members")
public class MemberReadController {

    private final MemberReadService memberReadService;

    public MemberReadController(MemberReadService memberReadService) {
        this.memberReadService = memberReadService;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        final List<MemberResultDto> memberResultDtos = memberReadService.findAllMembers();
        final List<MemberResponse> responses = memberResultDtos.stream()
                .map(MemberResponse::from)
                .collect(Collectors.toUnmodifiableList());

        return ResponseEntity.ok(responses);
    }

}
