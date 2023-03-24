package org.example.resource;

import org.example.config.SystemConstant;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Path("/messages/next")
@Component
public class MessagingResource {
    @GET
    public Response doLoad(@Suspended final AsyncResponse asyncResponse) throws ExecutionException, InterruptedException {
        //设置超时时间
        asyncResponse.setTimeout(SystemConstant.AsyncResponseTimeOutSecond, TimeUnit.SECONDS);
        //超时的处理方式
        asyncResponse.setTimeoutHandler(new TimeoutHandler() {
            @Override
            public void handleTimeout(AsyncResponse asyncResponse) {
                asyncResponse.resume(Response.serverError().entity("server timeout"));
            }
        });
        //调用完结事件
        asyncResponse.register(new CompletionCallback() {
            @Override
            public void onComplete(Throwable throwable) {
                if (throwable != null) {
                    throw new RuntimeException("business error");
                }
            }
        });
        //目标业务方法
        CompletableFuture<Response> future = veryExpensiveOperation();

        future.thenAccept(resp -> asyncResponse.resume(resp));
        //超时挂起就直接返回
        {
            //TODO 已经setTimeoutHandler 这几个方法有何意义，不加反而等待时间比较长
            if (asyncResponse.isCancelled()) {
                return Response.serverError().entity("server cancel").build();
            }
            if (asyncResponse.isSuspended()) {
                return Response.serverError().entity("server cancel").build();
            }
        }
        return future.get();
    }

    private CompletableFuture<Response> veryExpensiveOperation() {
        CompletableFuture<Response> completableFuture = new CompletableFuture<>();

        new Thread(() -> {
            //假设业务有1~10秒时间
            try {
                Thread.sleep(System.currentTimeMillis() % 10 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //异步响应的对象
            completableFuture.complete(Response.ok().entity("Completed").build());
        }).start();

        return completableFuture;
    }
}