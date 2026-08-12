package gamehub.game_Hub.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Repository.SubtitleRepository;
import gamehub.game_Hub.Response.SubtitleResponse;
import gamehub.game_Hub.Service.SubtitleService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubtitleServiceImpl implements SubtitleService {

  private final SubtitleRepository subtitleRepository;

  @Override
  public List<SubtitleResponse> findAllSubtitles() {
    return subtitleRepository.findAll()
        .stream()
        .map(subtitle -> new SubtitleResponse(subtitle.getId(), subtitle.getName()))
        .toList();
  }

}
