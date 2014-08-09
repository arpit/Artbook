package com.arpitonline.freeflow.artbook.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class Shot extends Object implements Parcelable {

	@Expose
	private Integer id;
	@Expose
	private String title;
	@Expose
	private Integer height;
	@Expose
	private Integer width;
	@Expose
	private Integer likes_count;
	@Expose
	private Integer comments_count;
	@Expose
	private Integer rebounds_count;
	@Expose
	private String url;
	@Expose
	private String short_url;
	@Expose
	private Integer views_count;
	@Expose
	private Integer rebound_source_id;
	@Expose
	private String image_url;
	@Expose
	private String image_teaser_url;
	@Expose
	private Player player;
	@Expose
	private String created_at;
	@Expose
	private String image_400_url;
	@Expose
	private String description;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Shot withId(Integer id) {
		this.id = id;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Shot withTitle(String title) {
		this.title = title;
		return this;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Shot withDescription(String description) {
		this.description = description;
		return this;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Shot withHeight(Integer height) {
		this.height = height;
		return this;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Shot withWidth(Integer width) {
		this.width = width;
		return this;
	}

	public Integer getLikes_count() {
		return likes_count;
	}

	public void setLikes_count(Integer likes_count) {
		this.likes_count = likes_count;
	}

	public Shot withLikes_count(Integer likes_count) {
		this.likes_count = likes_count;
		return this;
	}

	public Integer getComments_count() {
		return comments_count;
	}

	public void setComments_count(Integer comments_count) {
		this.comments_count = comments_count;
	}

	public Shot withComments_count(Integer comments_count) {
		this.comments_count = comments_count;
		return this;
	}

	public Integer getRebounds_count() {
		return rebounds_count;
	}

	public void setRebounds_count(Integer rebounds_count) {
		this.rebounds_count = rebounds_count;
	}

	public Shot withRebounds_count(Integer rebounds_count) {
		this.rebounds_count = rebounds_count;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Shot withUrl(String url) {
		this.url = url;
		return this;
	}

	public String getShort_url() {
		return short_url;
	}

	public void setShort_url(String short_url) {
		this.short_url = short_url;
	}

	public Shot withShort_url(String short_url) {
		this.short_url = short_url;
		return this;
	}

	public Integer getViews_count() {
		return views_count;
	}

	public void setViews_count(Integer views_count) {
		this.views_count = views_count;
	}

	public Shot withViews_count(Integer views_count) {
		this.views_count = views_count;
		return this;
	}

	public Integer getRebound_source_id() {
		return rebound_source_id;
	}

	public void setRebound_source_id(Integer rebound_source_id) {
		this.rebound_source_id = rebound_source_id;
	}

	public Shot withRebound_source_id(Integer rebound_source_id) {
		this.rebound_source_id = rebound_source_id;
		return this;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public Shot withImage_url(String image_url) {
		this.image_url = image_url;
		return this;
	}

	public String getImage_teaser_url() {
		return image_teaser_url;
	}

	public void setImage_teaser_url(String image_teaser_url) {
		this.image_teaser_url = image_teaser_url;
	}

	public Shot withImage_teaser_url(String image_teaser_url) {
		this.image_teaser_url = image_teaser_url;
		return this;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Shot withPlayer(Player player) {
		this.player = player;
		return this;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public Shot withCreated_at(String created_at) {
		this.created_at = created_at;
		return this;
	}

	public String getImage_400_url() {
		return image_400_url;
	}

	public void setImage_400_url(String image_400_url) {
		this.image_400_url = image_400_url;
	}

	public Shot withImage_400_url(String image_400_url) {
		this.image_400_url = image_400_url;
		return this;
	}
	
	@Override
	public boolean equals(Object other){
		if(! (other instanceof Shot)) return false;
		Shot oShot = (Shot)other;
		return oShot.image_url.equals(image_url) && (oShot.id == this.id);
	}


    protected Shot(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        title = in.readString();
        height = in.readByte() == 0x00 ? null : in.readInt();
        width = in.readByte() == 0x00 ? null : in.readInt();
        likes_count = in.readByte() == 0x00 ? null : in.readInt();
        comments_count = in.readByte() == 0x00 ? null : in.readInt();
        rebounds_count = in.readByte() == 0x00 ? null : in.readInt();
        url = in.readString();
        short_url = in.readString();
        views_count = in.readByte() == 0x00 ? null : in.readInt();
        rebound_source_id = in.readByte() == 0x00 ? null : in.readInt();
        image_url = in.readString();
        image_teaser_url = in.readString();
        player = (Player) in.readParcelable(Player.class.getClassLoader());
        created_at = in.readString();
        image_400_url = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(title);
        if (height == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(height);
        }
        if (width == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(width);
        }
        if (likes_count == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(likes_count);
        }
        if (comments_count == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(comments_count);
        }
        if (rebounds_count == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(rebounds_count);
        }
        dest.writeString(url);
        dest.writeString(short_url);
        if (views_count == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(views_count);
        }
        if (rebound_source_id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(rebound_source_id);
        }
        dest.writeString(image_url);
        dest.writeString(image_teaser_url);
        dest.writeParcelable(player, flags);
        dest.writeString(created_at);
        dest.writeString(image_400_url);
        dest.writeString(description);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Shot> CREATOR = new Parcelable.Creator<Shot>() {
        @Override
        public Shot createFromParcel(Parcel in) {
            return new Shot(in);
        }

        @Override
        public Shot[] newArray(int size) {
            return new Shot[size];
        }
    };
}