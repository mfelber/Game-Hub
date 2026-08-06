package gamehub.game_Hub.ServiceImpl.Admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Mapper.GameMapper;
import gamehub.game_Hub.Mapper.ReportMapper;
import gamehub.game_Hub.Mapper.UserMapper;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.Role;
import gamehub.game_Hub.Module.User.Wishlist;
import gamehub.game_Hub.Repository.ReportRepository;
import gamehub.game_Hub.Repository.UserLibraryRepository;
import gamehub.game_Hub.Repository.WishlistRepository;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Response.Admin.DashboardResponse;
import gamehub.game_Hub.Response.GamePreviewResponse;
import gamehub.game_Hub.Response.GameResponse;
import gamehub.game_Hub.Service.Admin.AdminService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements AdminService {

  private final GameRepository gameRepository;

  private final UserRepository userRepository;

  private final ReportRepository reportRepository;

  private final ReportMapper reportMapper;

  private final GameMapper gameMapper;

  private final UserMapper userMapper;

  private final WishlistRepository wishlistRepository;

  private final UserLibraryRepository userLibraryRepository;

  @Override
  public DashboardResponse loadDashboardData(final Authentication connectedUser, final int page,
      final int size) {
    // TODO change total reviews while implementing reviews
    return DashboardResponse.builder()
        .totalGames(gameRepository.count())
        .totalUsers(userRepository.countByRole(Role.USER))
        .pendingReports(reportRepository.countReportsByStatus_Id(1L))
        .totalReviews(100L)
        .recentUsers(userRepository.findTop5ByRoleNotOrderByCreatedAtDesc(Role.ADMIN).stream().map(userMapper::toRecentUserResponse).toList())
        .latestReports(reportRepository.findTop5ByOrderByCreatedAtDesc().stream().map(reportMapper::toReportResponse).toList())
        .build();
  }

  @Override
  public PageResponse<GamePreviewResponse> getAllGames(final int page, final int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

    Page<Game> games = gameRepository.findAll(pageable);
    List<GamePreviewResponse> gamesResponse = games.stream().map(gameMapper::toGamePreviewResponse).toList();

    return new PageResponse<>(
        gamesResponse,
        games.getNumber(),
        games.getSize(),
        games.getTotalElements(),
        games.getTotalPages(),
        games.isFirst(),
        games.isLast()
    );
  }

  @Override
  public GameResponse getGameInfo(final Long gameId) {
    Game game = gameRepository.findById(gameId).orElseThrow(() -> new EntityNotFoundException("Game with id " + gameId + " was not found"));
    return gameMapper.toGameResponse(game);
  }

  @Override
  @Transactional
  public void deleteGame(final Long gameId) {
    Game game = gameRepository.findById(gameId).orElseThrow(() -> new EntityNotFoundException("Game with id " + gameId + " was not found"));
    wishlistRepository.deleteAllByGame(game);
    userLibraryRepository.deleteAllByGame(game);
    gameRepository.delete(game);
  }

}
